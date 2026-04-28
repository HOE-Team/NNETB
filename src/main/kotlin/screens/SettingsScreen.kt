/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.*
import theme.isValidHex
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// (HEX 输入实现，不再使用预设颜色选项)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SettingsScreen(
    isDarkTheme: Boolean = false,
    onThemeChange: (Boolean) -> Unit = {},
    selectedColor: String = "",
    onColorChange: (String) -> Unit = {}
) {
    var localDarkTheme by remember { mutableStateOf(isDarkTheme) }
    var hexInput by remember { mutableStateOf(selectedColor) }
    var saveStateMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // 程序外观标题（缩小）
        Text(
            text = "程序外观",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 设置项1：深色主题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "深色主题",
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "深色主题",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Switch(
                checked = localDarkTheme,
                onCheckedChange = { newValue ->
                    localDarkTheme = newValue
                    onThemeChange(newValue)
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 设置项2：HEX 颜色输入与保存
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "颜色主题",
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "颜色主题 (HEX)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "输入 6 位 HEX，例如：#6750A4 或 6750A4",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val isValid = remember(hexInput) { isValidHex(hexInput.trim()) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = hexInput,
                    onValueChange = { hexInput = it; saveStateMessage = null },
                    singleLine = true,
                    isError = hexInput.isNotBlank() && !isValid,
                    placeholder = { Text("#RRGGBB") },
                    modifier = Modifier.width(180.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    val cleaned = hexInput.trim()
                    if (cleaned.isNotBlank() && isValid) {
                        onColorChange(if (cleaned.startsWith("#")) cleaned else "#${cleaned}")
                        saveStateMessage = "已保存"
                    } else if (cleaned.isBlank()) {
                        onColorChange("")
                        saveStateMessage = "已重置为默认"
                    } else {
                        saveStateMessage = "无效的 HEX"
                    }
                }) {
                    Text("保存")
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(12.dp))

        saveStateMessage?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
