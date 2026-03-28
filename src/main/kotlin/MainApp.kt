package main.kotlin

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.singleWindowApplication
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import components.AppScaffold
import components.NavRail
import screens.HomeScreen
import screens.ToolsScreen
import screens.SettingsScreen
import screens.AboutScreen
import theme.AppTheme
import config.loadConfig
import config.saveConfig
import config.AppConfig
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import java.awt.Dimension

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NNETB's Not Everything Toolbox",
        icon = painterResource("img/logo.png"),  // 注意：这里需要使用相对于 resources 的路径
        state = WindowState()
    ) {
        var selectedNavIndex by remember { mutableStateOf(0) }

        val topBarTitle = when (selectedNavIndex) {
            1 -> "工具"
            2 -> "设置"
            3 -> "关于"
            else -> "概览"
        }

        // load persisted settings
        val loaded = loadConfig()
        var isDark by remember { mutableStateOf(loaded.dark) }
        var seedHex by remember { mutableStateOf<String?>(loaded.color) }

        AppTheme(darkTheme = isDark, seedHex = seedHex) {
            AppScaffold(
                startBar = { NavRail(onSelection = { selectedNavIndex = it }) },
                topBarTitle = topBarTitle,
                content = {
                    when (selectedNavIndex) {
                        1 -> ToolsScreen()
                        2 -> SettingsScreen(
                            isDarkTheme = isDark,
                            onThemeChange = { newDark ->
                                isDark = newDark
                                saveConfig(AppConfig(dark = isDark, color = seedHex))
                            },
                            selectedColor = seedHex ?: "",
                            onColorChange = { hex ->
                                seedHex = if (hex.isBlank()) null else hex
                                saveConfig(AppConfig(dark = isDark, color = seedHex))
                            }
                        )
                        3 -> AboutScreen()
                        else -> HomeScreen()
                    }
                },
                testAprilFools = false
            )
        }
    }
}