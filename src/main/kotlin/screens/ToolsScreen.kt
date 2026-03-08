package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
    val url: String
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
        ToolItem("AIDA64", "系统硬件检测和诊断", "https://www.aida64.com/"),
        ToolItem("CPU-Z", "CPU 信息检测工具", "https://www.cpuid.com/softwares/cpu-z.html"),
        ToolItem("GPU-Z", "GPU 信息检测工具", "https://www.techpowerup.com/gpuz/"),
        ToolItem("HWiNFO", "硬件信息实时监控", "https://www.hwinfo.com/")
    )
    ToolCardGrid(tools)
}

@Composable
fun DriverManagementToolContent() {
    val tools = listOf(
        ToolItem("DDU", "显卡驱动卸载工具", "https://www.guru3d.com/files-details/nvidia-driver-uninstaller.html")
    )
    ToolCardGrid(tools)
}

@Composable
fun MediaToolContent() {
    val tools = listOf(
        ToolItem("VLC", "多媒体播放器", "https://www.videolan.org/vlc/")
    )
    ToolCardGrid(tools)
}

@Composable
fun FileToolContent() {
    val tools = listOf(
        ToolItem("FreeFileSync", "文件同步工具", "https://freefilesync.org/"),
        ToolItem("FileLight","磁盘占用查看器","https://apps.kde.org/zh-cn/filelight/"),

    )
    ToolCardGrid(tools)
}

@Composable
fun DeveloperToolContent() {
    val tools = listOf(
        ToolItem("Resource Hacker", "资源编辑工具", "http://www.angusj.com/resourcehacker/"),
        ToolItem("Git","版本控制工具","https://git-scm.com/install/windows"),
        ToolItem("Git LFS","Git大文件存储","https://git-lfs.com/")
    )
    ToolCardGrid(tools)
}

@Composable
fun OtherToolContent() {
    val tools = listOf(
        ToolItem("Rufus", "镜像烧录工具", "https://rufus.ie/zh/"),
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
            .height(140.dp)
            .clickable {
                openToolWebsite(tool.url)
            },
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
        }
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
