/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import utils.SystemOverview
import java.io.File
import java.io.FileInputStream
import androidx.compose.ui.res.loadImageBitmap

@Composable
fun SystemOverviewCard(
    overview: SystemOverview,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            // Background image if available
            val wallpaperBitmap = remember(overview.wallpaperPath) {
                try {
                    val p = overview.wallpaperPath
                    if (!p.isNullOrBlank()) {
                        val f = File(p)
                        if (f.exists()) {
                            val stream = FileInputStream(f)
                            val bmp = loadImageBitmap(stream)
                            stream.close()
                            bmp
                        } else null
                    } else null
                } catch (_: Exception) {
                    null
                }
            }

            if (wallpaperBitmap != null) {
                Image(
                    bitmap = wallpaperBitmap,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Left (transparent) -> Right (opaque) overlay
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 1.0f)
                        )
                    )
                )
            )

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                Text(
                    text = "系统概览",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = overview.computerName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = "操作系统版本: ${overview.osVersion}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "系统架构: ${overview.architecture}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "平台: ${overview.platform}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
