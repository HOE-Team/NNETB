/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI
import java.time.LocalDate

/**
 * 检查当前日期是否为4月1日（愚人节）
 */
fun isAprilFoolsDay(): Boolean {
    val today = LocalDate.now()
    return today.monthValue == 4 && today.dayOfMonth == 1
}

/**
 * 测试模式：总是返回true，用于验证组件
 */
fun isAprilFoolsDayTest(): Boolean {
    return true
}

/**
 * 打开B站视频链接
 */
fun openBilibiliVideo() {
    try {
        val desktop = Desktop.getDesktop()
        desktop.browse(URI("https://www.bilibili.com/video/BV1GJ411x7h7"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 愚人节警告卡片组件
 * 只在4月1日显示
 * 点击按钮后文字变为"April Fool！"，3秒后打开链接
 */
@Composable
fun AprilFoolsWarningCard(
    modifier: Modifier = Modifier,
    testMode: Boolean = false
) {
    val shouldShow = if (testMode) isAprilFoolsDayTest() else isAprilFoolsDay()
    
    if (!shouldShow) {
        return
    }
    
    // 状态管理
    var warningText by remember { mutableStateOf("你知道吗？NNETB包含木马和勒索病毒！") }
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),  // 减少外边距
        shape = RoundedCornerShape(8.dp),  // 减小圆角
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3CD), // 浅黄色背景，类似警告
            contentColor = Color(0xFF856404) // 深黄色文字
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)  // 减小阴影
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),  // 减少内边距
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 左侧：图标和文本
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)  // 减少间距
            ) {
                // 警告图标（缩小）
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "警告",
                    modifier = Modifier.size(24.dp),  // 缩小图标
                    tint = Color(0xFF856404)
                )
                
                // 文本
                Text(
                    text = warningText,
                    style = MaterialTheme.typography.bodyMedium.copy(  // 使用较小字体
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp  // 减小字体大小
                    )
                )
            }
            
            // 右侧：链接按钮（缩小）
            TextButton(
                onClick = {
                    if (!isClicked) {
                        isClicked = true
                        warningText = "April Fool！"
                        
                        // 3秒后打开链接
                        coroutineScope.launch {
                            delay(3000) // 3秒延迟
                            openBilibiliVideo()
                            // 重置状态（可选）
                            warningText = "你知道吗？NNETB包含木马和勒索病毒！"
                            isClicked = false
                        }
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isClicked) Color(0xFF28A745) else Color(0xFF007BFF)  // 点击后变绿色
                ),
                modifier = Modifier.padding(start = 8.dp),  // 添加左边距
                enabled = !isClicked  // 点击后禁用按钮，防止重复点击
            ) {
                Text(
                    text = "我该怎么办？",
                    style = MaterialTheme.typography.bodySmall.copy(  // 使用较小字体
                        fontSize = 12.sp  // 减小字体大小
                    )
                )
            }
        }
    }
}
