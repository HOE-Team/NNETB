/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.CPUStatCard
import components.RAMStatCard
import components.GPUStatCard
import components.SystemOverviewCard
import utils.SystemInfoProvider
import utils.SystemInfoSnapshot
import utils.SystemOverview
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var systemInfo by remember { mutableStateOf(SystemInfoProvider.getSystemInfo()) }
    var systemOverview by remember { mutableStateOf(SystemInfoProvider.getSystemOverview()) }
    
    // Update system info every 1 second
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            systemInfo = SystemInfoProvider.getSystemInfo()
            systemOverview = SystemInfoProvider.getSystemOverview()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // System overview card (centered, not full width)
        // System overview card (adaptive width)
        SystemOverviewCard(
            overview = systemOverview,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Two-column layout for stat cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CPU Card (left column)
            CPUStatCard(
                model = systemInfo.cpu.model,
                usage = systemInfo.cpu.usage,
                stepping = systemInfo.cpu.stepping,
                currentFreq = systemInfo.cpu.currentFreq,
                modifier = Modifier.weight(1f)
            )
            
            // RAM Card (right column)
            RAMStatCard(
                frequency = systemInfo.ram.frequency,
                used = systemInfo.ram.used,
                total = systemInfo.ram.total,
                usage = systemInfo.ram.usage,
                modifier = Modifier.weight(1f)
            )
        }
        
        // GPU + Disk side by side
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GPUStatCard(
                gpus = systemInfo.gpus,
                modifier = Modifier.weight(1f)
            )
            components.DiskStatCard(
                disks = systemInfo.disks,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
