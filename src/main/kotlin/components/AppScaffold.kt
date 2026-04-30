/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    startBar: @Composable () -> Unit = {},
    topBarTitle: String = "概览",
    content: @Composable () -> Unit = {},
    testAprilFools: Boolean = false
) {
    MaterialTheme {
        Scaffold(
        ) { paddingValues ->
            Row(modifier = Modifier.padding(paddingValues)) {
                // Left rail occupies full height
                Box(modifier = Modifier.fillMaxHeight()) {
                    startBar()
                }

                // Right side: TopBar at top, then content fills remaining space
                Column(modifier = Modifier.fillMaxSize()) {
                    TopBar(title = topBarTitle)
                    // 愚人节警告卡片（只在4月1日显示，测试模式可强制显示）
                    AprilFoolsWarningCard(testMode = testAprilFools)
                    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        content()
                    }
                }
            }
        }
    }
}
