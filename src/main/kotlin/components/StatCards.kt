/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import java.util.Locale

@Composable
fun CircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Float = 80f,
    strokeWidth: Float = 4f,
    label: String = ""
) {
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier.size(size.dp),
            strokeWidth = strokeWidth.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = "${progress.roundToInt()}%",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp
        )
    }
}

@Composable
fun StatCard(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun CPUStatCard(
    model: String,
    usage: Double,
    stepping: String = "Unknown",
    currentFreq: Double = 0.0,
    modifier: Modifier = Modifier
) {
    StatCard(
        title = "CPU",
        modifier = modifier,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    progress = usage.toFloat(),
                    modifier = Modifier.weight(0.3f)
                )
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "型号: $model",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                    Text(
                        text = "步进: $stepping",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "频率: ${String.format("%.2f", currentFreq)} GHz",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    )
}

@Composable
fun RAMStatCard(
    frequency: Long,
    used: Double,
    total: Double,
    usage: Double,
    modifier: Modifier = Modifier
) {
    StatCard(
        title = "内存",
        modifier = modifier,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    progress = usage.toFloat(),
                    modifier = Modifier.weight(0.3f)
                )
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (frequency > 0) "频率: ${frequency} MHz" else "频率: 未知",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "占用: %.2f / %.2f GB", used, total),
                        style = MaterialTheme.typography.bodySmall
                    )
                        // Removed textual percentage; percentage remains inside circular indicator
                }
            }
        }
    )
}

@Composable
fun GPUStatCard(
    gpus: List<utils.GPUInfo>,
    modifier: Modifier = Modifier
) {
    StatCard(
        title = "已安装的GPU",
        modifier = modifier,
        content = {
            if (gpus.isEmpty()) {
                // No GPU installed
                Text(
                    text = "无GPU被安装或驱动程序未安装",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (gpus.size == 1) {
                // Single GPU - show only model name
                val gpu = gpus[0]
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = gpu.model,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
            } else {
                // Multiple GPUs - show all model names
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    gpus.forEach { gpu ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = gpu.model,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DiskStatCard(
    disks: List<utils.DiskInfo>,
    modifier: Modifier = Modifier
) {
    if (disks.isEmpty()) return

    StatCard(
        title = "磁盘",
        modifier = modifier,
        content = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                disks.forEach { disk ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "${disk.name} - ${disk.model}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(((disk.usage / 100.0).toFloat()), modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f GB / %.2f GB", disk.usedGB, disk.totalGB),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    )
}
