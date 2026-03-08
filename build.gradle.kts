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
        nativeDistributions {
            // For Windows: generate both EXE and MSI
            targetFormats(TargetFormat.Exe, TargetFormat.Msi)
            packageName = "EverythingFineToolbox"
            packageVersion = "1.0.0"
            
            // Windows installer: prefer system-wide (per-machine) install
            windows {
                perUserInstall = false
                menuGroup = "Everything Fine Toolbox"
                dirChooser = true
            }
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

// Task to build installer using Inno Setup
tasks.register<Exec>("buildInstallerInnoSetup") {
    group = "distribution"
    description = "Build Windows installer using Inno Setup"
    dependsOn("packageDistributionForCurrentOS")
    
    val innoSetupPath = "D:\\Program Files (x86)\\Inno Setup 6\\ISCC.exe"
    val installerScriptPath = file("installer.iss").absolutePath
    val outputDir = file("${project.buildDir}/compose/binaries/main/installer").absolutePath
    
    // Ensure output directory exists
    doFirst {
        file("${project.buildDir}/compose/binaries/main/installer").mkdirs()
        
        if (!file(innoSetupPath).exists()) {
            throw GradleException("Inno Setup not found at $innoSetupPath\nPlease install Inno Setup 6 from: https://jrsoftware.org/isdl.php")
        }
        println("Building Windows installer with Inno Setup...")
        println("Script: $installerScriptPath")
    }
    
    // Call Inno Setup compiler
    executable = innoSetupPath
    args = listOf(installerScriptPath)
    
    // Set working directory to project root so relative paths in .iss work
    workingDir = project.projectDir
    
    doLast {
        val exeFile = file("${outputDir}/EverythingFineToolbox-1.0.0-Setup.exe")
        if (exeFile.exists()) {
            println("✓ Installer created: ${exeFile.absolutePath}")
            println("  Size: ${exeFile.length() / (1024 * 1024)} MB")
        } else {
            println("⚠ Note: Check installer output - file may be at different location")
        }
    }
}

// Convenience task for full packaging
tasks.register("packageApplication") {
    group = "distribution"
    description = "Build standalone Windows installed application"
    dependsOn("buildInstallerInnoSetup")
}
