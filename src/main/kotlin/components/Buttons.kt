/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable

@Composable
fun PrimaryFAB(onClick: () -> Unit = {}) {
    FloatingActionButton(onClick = onClick) {
        Text("+")
    }
}

@Composable
fun SampleButtons() {
    Button(onClick = {}) {
        Text("Primary")
    }
}
