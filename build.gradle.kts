import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.io.File

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.1"
}

repositories {
    google()
    mavenCentral()
}

// Required for JetBrains Compose artifacts
repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3:1.6.1")
    // Material Icons (filled/extended)
    implementation("org.jetbrains.compose.material:material-icons-extended:1.6.1")
    implementation("com.github.oshi:oshi-core:6.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "main.kotlin.MainAppKt"
        
        // Try to set icon directly through Compose Desktop DSL
        nativeDistributions {
            // For Windows: generate EXE (MSI packaging replaced by Inno Setup)
            targetFormats(TargetFormat.Exe)
            packageName = "NNETBsNotEverythingToolbox"
            packageVersion = "1.1.1"
            
            // Windows installer: prefer system-wide (per-machine) install
            windows {
                perUserInstall = false
                menuGroup = "NNETB's Not Everything Toolbox"
                dirChooser = true
                iconFile.set(project.file("images/logo.ico"))
            }
            
            // Try to set application icon
            appResourcesRootDir.set(project.layout.projectDirectory.dir("images"))
        }
    }
}

// Copy /res directory to distribution after evaluation (when Compose plugin creates tasks)
afterEvaluate {
    try {
        tasks.named("createRuntimeImage") {
            doLast {
                println("Copying /res directory to app...")
                copy {
                    from(file("res"))
                    into(file("${project.buildDir}/compose/binaries/main/app/res"))
                }
            }
        }
    } catch (e: Exception) {
        println("Warning: Could not configure resource copying: ${e.message}")
    }
}

// Ensure /res is included in the distribution before packaging
tasks.register<Copy>("includeResInDistribution") {
    group = "distribution"
    description = "Copy res folder into compose distribution app folder"
    from(file("res"))
    into(file("${project.buildDir}/compose/binaries/main/app/res"))
}

// Make packageDistributionForCurrentOS depend on copying resources
afterEvaluate {
    try {
        tasks.named("packageDistributionForCurrentOS") {
            dependsOn("includeResInDistribution")
        }
    } catch (e: Exception) {
        println("Warning: Could not wire includeResInDistribution into packaging: ${e.message}")
    }
}

// Post-process generated executable to embed custom icon using rcedit (Windows tool)
// you can download rcedit from https://github.com/electron/rcedit/releases and put it on PATH

tasks.register("applyIconToExe") {
    group = "distribution"
    description = "Replace icon in generated executable with images/logo.ico using rcedit (if available)"
    dependsOn("packageDistributionForCurrentOS")

    doLast {
        // The executable is generated in the exe directory, not app directory
        val exeFileName = "NNETBsNotEverythingToolbox-1.1.0.exe"
        val exeFile = file("${project.buildDir}/compose/binaries/main/exe/$exeFileName")
        if (!exeFile.exists()) {
            println("Executable not found at ${exeFile.absolutePath}, skipping icon application")
            // Try to find the executable in the app directory as fallback
            val altExeFile = file("${project.buildDir}/compose/binaries/main/app/$exeFileName")
            if (altExeFile.exists()) {
                println("Found executable at alternative location: ${altExeFile.absolutePath}")
                applyIconToFile(altExeFile)
            }
            return@doLast
        }
        
        applyIconToFile(exeFile)
    }
}

fun applyIconToFile(exeFile: File) {
    // Resolve rcedit: project property 'rceditPath', env 'RCEDIT_PATH', or look on PATH via 'where'
    val candidate = listOf(
        project.findProperty("rceditPath")?.toString(),
        System.getenv("RCEDIT_PATH")
    ).firstOrNull { !it.isNullOrBlank() } ?: run {
        try {
            val where = ProcessBuilder("where", "rcedit").start()
            val output = where.inputStream.bufferedReader().readText().trim()
            val error = where.errorStream.bufferedReader().readText().trim()
            where.waitFor()
            
            // Only return if exit code is 0 (success) and output is not empty
            // Also check that output doesn't contain error messages (like Chinese error text)
            if (where.exitValue() == 0 && output.isNotBlank() && !output.contains("信息") && !output.contains("找不到")) {
                output.lineSequence().firstOrNull()?.trim()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    if (candidate.isNullOrBlank()) {
        println("rcedit not found on PATH and RCEDIT_PATH not set; skipping icon application.")
        println("To enable icon embedding, install rcedit from https://github.com/electron/rcedit/releases")
        println("and add to PATH or set -PrceditPath=/path/to/rcedit.exe or env RCEDIT_PATH")
        return
    }

    val rceditPath = candidate
    println("Applying icon using: $rceditPath")

    try {
        val proc = ProcessBuilder(rceditPath, exeFile.absolutePath, "--set-icon", file("images/logo.ico").absolutePath)
            .inheritIO()
            .directory(project.projectDir)
            .start()
        val exit = proc.waitFor()
        if (exit != 0) {
            println("Warning: rcedit failed with exit code $exit, but continuing build...")
            // Don't throw exception, just log warning
        } else {
            println("Icon applied successfully to ${exeFile.name}.")
        }
    } catch (e: Exception) {
        println("Warning: Failed to apply icon: ${e.message}, but continuing build...")
        // Don't throw exception, just log warning
    }
}

// Hook post-processing after packaging task exists
afterEvaluate {
    tasks.findByName("packageDistributionForCurrentOS")?.let {
        it.finalizedBy("applyIconToExe")
    }
}

// Task to build installer using Inno Setup
tasks.register("buildInstallerInnoSetup") {
    group = "distribution"
    description = "Build Windows installer using Inno Setup (ISCC). Will look for ISCC on PATH, env INNO_SETUP_PATH, or property -PinnoPath."
    dependsOn("packageDistributionForCurrentOS")

    doLast {
        val os = System.getProperty("os.name").lowercase()
        if (!os.contains("windows")) {
            println("Skipping Inno installer: not running on Windows")
            return@doLast
        }

        // Resolve Inno Setup compiler: project property 'innoPath', env 'INNO_SETUP_PATH', or look on PATH via 'where'
        val candidate = listOf(
            project.findProperty("innoPath")?.toString(),
            System.getenv("INNO_SETUP_PATH")
        ).firstOrNull { !it.isNullOrBlank() } ?: run {
            try {
                val where = ProcessBuilder("where", "iscc").start()
                val output = where.inputStream.bufferedReader().readText().trim()
                val error = where.errorStream.bufferedReader().readText().trim()
                where.waitFor()
                
                // Only return if exit code is 0 (success) and output is not empty
                // Also check that output doesn't contain error messages (like Chinese error text)
                if (where.exitValue() == 0 && output.isNotBlank() && !output.contains("信息") && !output.contains("找不到")) {
                    output.lineSequence().firstOrNull()?.trim()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }

        if (candidate.isNullOrBlank()) {
            throw GradleException("Inno Setup compiler (ISCC) not found. Install Inno Setup or set INNO_SETUP_PATH or -PinnoPath=/path/to/ISCC.exe")
        }

        val isccPath = candidate
        println("Using Inno Setup compiler: $isccPath")

        val script = file("installer.iss")
        if (!script.exists()) throw GradleException("installer.iss not found at ${script.absolutePath}")

        file("${project.buildDir}/compose/binaries/main/installer").mkdirs()

        val proc = ProcessBuilder(isccPath, script.absolutePath)
            .inheritIO()
            .directory(project.projectDir)
            .start()
        val exit = proc.waitFor()
        if (exit != 0) throw GradleException("ISCC failed with exit code $exit")

        println("Inno installer build finished.")
    }
}

// Convenience task for full packaging
tasks.register("packageApplication") {
    group = "distribution"
    description = "Build standalone Windows installed application"
    dependsOn("buildInstallerInnoSetup")
}
