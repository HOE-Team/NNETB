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
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Description
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.Desktop
import java.net.URI

data class ToolItem(
    val name: String,
    val description: String,
    val url: String,
    val isProprietarySoftware: Boolean = false,
    val licenseUrl: String? = null,
    val eulaUrl: String? = null,
    val licenseType: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        "系统信息与硬件监控工具",
        "驱动程序管理工具",
        "媒体工具",
        "文件工具",
        "开发人员工具",
        "其它工具"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Tabs
        TabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontSize = 12.sp) }
                )
            }
        }

        // Tab content
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (selectedTab) {
                0 -> SystemMonitoringToolContent()
                1 -> DriverManagementToolContent()
                2 -> MediaToolContent()
                3 -> FileToolContent()
                4 -> DeveloperToolContent()
                5 -> OtherToolContent()
            }
        }
    }
}

@Composable
fun SystemMonitoringToolContent() {
    val tools = listOf(
        ToolItem("LibreHardwareMonitor", "开源硬件信息检测工具", "https://github.com/LibreHardwareMonitor/LibreHardwareMonitor/releases/latest",
            licenseUrl = "https://github.com/LibreHardwareMonitor/LibreHardwareMonitor/blob/master/LICENSE",
            licenseType = "MPL-2.0"),
        ToolItem("SidebarDiagnostics", "侧边栏硬件状态显示", "https://github.com/ArcadeRenegade/SidebarDiagnostics/releases/latest",
            licenseUrl = "https://github.com/ArcadeRenegade/SidebarDiagnostics/blob/master/LICENSE.md",
            licenseType = "GPL-3.0"),
        ToolItem("AIDA64", "系统硬件检测和诊断", "https://www.aida64.com/",
            isProprietarySoftware = true),
        ToolItem("CPU-Z", "CPU 信息检测工具", "https://www.cpuid.com/softwares/cpu-z.html",
            isProprietarySoftware = true),
        ToolItem("GPU-Z", "GPU 信息检测工具", "https://www.techpowerup.com/gpuz/",
            isProprietarySoftware = true),
        ToolItem("HWiNFO", "硬件信息实时监控", "https://www.hwinfo.com/", 
            isProprietarySoftware = true),
        ToolItem("Process Monitor", "进程监控工具", "https://learn.microsoft.com/zh-cn/sysinternals/downloads/procmon", 
            isProprietarySoftware = true,
            eulaUrl = "https://learn.microsoft.com/zh-cn/sysinternals/license-terms")
    )
    ToolCardGrid(tools)
}

@Composable
fun DriverManagementToolContent() {
    val tools = listOf(
        ToolItem("DDU", "显卡驱动卸载工具", "https://www.guru3d.com/files-details/nvidia-driver-uninstaller.html",
            isProprietarySoftware = true),
        ToolItem("IObit Driver Booster", "驱动更新工具", "https://www.iobit.com/en/driver-booster.php", 
            isProprietarySoftware = true,
            eulaUrl = "https://www.iobit.com/en/eula.php")
    )
    ToolCardGrid(tools)
}

@Composable
fun MediaToolContent() {
    val tools = listOf(
        ToolItem("VLC", "多媒体播放器", "https://www.videolan.org/vlc/", 
            licenseUrl = "https://www.videolan.org/legal.html",
            licenseType = "GPL-2.0"),
        ToolItem("MPC-HC","轻量级媒体播放器", "https://mpc-hc.org/",
            licenseUrl = "https://github.com/clsid2/mpc-hc/blob/develop/COPYING.txt",
            licenseType = "GPL-3.0"),
        ToolItem("SMPlayer","多功能播放器", "https://github.com/smplayer-dev/smplayer/releases/latest",
            licenseUrl = "https://github.com/smplayer-dev/smplayer/blob/master/Copying.txt",
            licenseType = "GPL-2.0"),
        ToolItem("Kodi","家庭影院中心", "https://kodi.tv/",
            licenseUrl = "https://github.com/xbmc/xbmc/blob/master/LICENSES/GPL-2.0-or-later",
            licenseType = "GPL-2.0"),
        ToolItem("OpenShot","开源剪辑软件", "https://www.openshot.org/zh-hans/",
            licenseUrl = "https://github.com/OpenShot/openshot-qt/blob/develop/COPYING",
            licenseType = "GPL-3.0"),
        ToolItem("kdenlive","KDE 非线性视频编辑器", "https://kdenlive.org/zh-cn/",
            licenseUrl = "https://invent.kde.org/multimedia/kdenlive/-/blob/master/COPYING",
            licenseType = "GPL-3.0"),
        ToolItem("OBS Studio","专业录制软件", "https://obsproject.com/zh-cn/download",
            licenseUrl = "https://github.com/obsproject/obs-studio/blob/master/COPYING",
            licenseType = "GPL-2.0"),
    )
    ToolCardGrid(tools)
}

@Composable
fun FileToolContent() {
    val tools = listOf(
        ToolItem("FreeFileSync", "文件同步工具", "https://freefilesync.org/",
            isProprietarySoftware = true,
            eulaUrl = "https://freefilesync.org/faq.php#donation-edition"),
        ToolItem("FileLight", "磁盘占用查看器", "https://apps.kde.org/zh-cn/filelight/",
            licenseUrl = "https://invent.kde.org/utilities/filelight/-/tree/master/LICENSES?ref_type=heads",
            licenseType = "GPL-3.0")
    )
    ToolCardGrid(tools)
}

@Composable
fun DeveloperToolContent() {
    val tools = listOf(
        ToolItem("Resource Hacker", "资源编辑工具", "http://www.angusj.com/resourcehacker/",
            isProprietarySoftware = true),
        ToolItem("Git", "版本控制工具", "https://git-scm.com/install/windows",
            licenseUrl = "https://github.com/git/git/blob/master/COPYING",
            licenseType = "GPL-2.0"),
        ToolItem("Git LFS", "Git大文件存储", "https://git-lfs.com/",
            licenseUrl = "https://github.com/git-lfs/git-lfs/blob/main/LICENSE.md",
            licenseType = "MIT")
    )
    ToolCardGrid(tools)
}

@Composable
fun OtherToolContent() {
    val tools = listOf(
        ToolItem("Rufus", "镜像烧录工具", "https://rufus.ie/zh/",
            licenseUrl = "https://github.com/pbatard/rufus/blob/master/LICENSE.txt",
            licenseType = "GPL-3.0"),
    )
    ToolCardGrid(tools)
}

@Composable
fun ToolCardGrid(tools: List<ToolItem>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Group tools into pairs (2 per row)
        tools.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { tool ->
                    ToolCard(
                        tool = tool,
                        modifier = Modifier.weight(1f)
                    )
                }
                // If odd number, add spacer to balance
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ToolCard(tool: ToolItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(180.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tool.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = tool.description,
                style = MaterialTheme.typography.bodySmall
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 专有软件警告或开源软件许可证按钮
            if (tool.isProprietarySoftware) {
                // 专有软件：警告文本 + 下载按钮 + 查看EULA按钮
                // 警告文本放在按钮上方
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "警告",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "专有软件，请遵守其条款",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                // 根据是否有EULA URL决定按钮布局
                if (tool.eulaUrl != null) {
                    // 有EULA URL：下载按钮和查看EULA按钮并排放置
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 下载按钮
                        Button(
                            onClick = { openToolWebsite(tool.url) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "下载", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("下载", fontSize = 12.sp)
                        }
                        
                        // 查看EULA按钮（和开源软件的许可证按钮样式一样）
                        OutlinedButton(
                            onClick = { openToolWebsite(tool.eulaUrl) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(Icons.Default.Description, contentDescription = "查看条款", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("查看条款", fontSize = 12.sp)
                        }
                    }
                } else {
                    // 没有EULA URL：只显示下载按钮
                    Button(
                        onClick = { openToolWebsite(tool.url) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "下载", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("下载", fontSize = 12.sp)
                    }
                }
            } else {
                // 开源软件：许可证类型显示 + 下载按钮和查看许可证按钮
                val licenseUrl = tool.licenseUrl ?: getDefaultLicenseUrl(tool.url)
                
                // 显示许可证类型（如果有）
                if (tool.licenseType != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "许可证类型",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tool.licenseType,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                if (licenseUrl != null) {
                    // 两个按钮并排放置
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 下载按钮
                        Button(
                            onClick = { openToolWebsite(tool.url) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "下载", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("下载", fontSize = 12.sp)
                        }
                        
                        // 查看许可证按钮
                        OutlinedButton(
                            onClick = { openToolWebsite(licenseUrl) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(Icons.Default.Description, contentDescription = "查看许可证", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("查看许可证", fontSize = 12.sp)
                        }
                    }
                } else {
                    // 没有许可证URL，只显示下载按钮
                    Button(
                        onClick = { openToolWebsite(tool.url) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "下载", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("下载", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun getDefaultLicenseUrl(toolUrl: String): String? {
    // 尝试从工具URL推断许可证URL
    return when {
        toolUrl.contains("github.com") -> {
            // GitHub仓库，添加/LICENSE路径
            toolUrl.removeSuffix("/") + "/blob/main/LICENSE"
        }
        toolUrl.contains("gitlab.com") -> {
            // GitLab仓库
            toolUrl.removeSuffix("/") + "/-/blob/main/LICENSE"
        }
        else -> null
    }
}

private fun openToolWebsite(url: String) {
    try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI(url))
            println("Opened website: $url")
        } else {
            println("Desktop browsing is not supported on this platform")
        }
    } catch (e: Exception) {
        println("Failed to open website $url: ${e.message}")
        e.printStackTrace()
    }
}
