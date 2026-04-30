/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String = "概览") {
    SmallTopAppBar(
        modifier = Modifier.height(48.dp),
        title = {
            BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                val density = LocalDensity.current
                val maxPx = with(density) { maxWidth.toPx() }
                val measurer = rememberTextMeasurer()
                var fontSize = 24.sp
                val minFont = 12.sp
                var measuredWidth = measurer.measure(AnnotatedString(title), style = TextStyle(fontSize = fontSize)).size.width.toFloat()
                while (measuredWidth > maxPx && fontSize > minFont) {
                    fontSize = (fontSize.value - 1).coerceAtLeast(minFont.value).let { it.sp }
                    measuredWidth = measurer.measure(AnnotatedString(title), style = TextStyle(fontSize = fontSize)).size.width.toFloat()
                }

                Text(
                    text = title,
                    style = TextStyle(fontSize = fontSize, fontWeight = FontWeight.Medium),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}
