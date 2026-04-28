/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavRail(onSelection: (Int) -> Unit = {}) {
    var selected by remember { mutableStateOf(0) }
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            NavigationRailItem(selected = selected == 0, onClick = { selected = 0; onSelection(0) }, icon = { Icon(Icons.Filled.Home, contentDescription = "Home") }, label = { Text("概览") })
            NavigationRailItem(selected = selected == 1, onClick = { selected = 1; onSelection(1) }, icon = { Icon(Icons.Filled.Build, contentDescription = "Tools") }, label = { Text("工具") })
            NavigationRailItem(selected = selected == 2, onClick = { selected = 2; onSelection(2) }, icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") }, label = { Text("设置") })
            NavigationRailItem(selected = selected == 3, onClick = { selected = 3; onSelection(3) }, icon = { Icon(Icons.Filled.Info, contentDescription = "About") }, label = { Text("关于") })
        }
    }
}
