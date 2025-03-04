package ir.thesinaa.geartick

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.thesinaa.geartick.fpsmonitor.FpsMonitor
import ir.thesinaa.geartick.networkmonitor.NetworkUsageMonitor
import ir.thesinaa.geartick.rammonitor.RamMonitor
import kotlin.math.roundToInt

@Composable
fun GearTick(
    modifier: Modifier = Modifier,
    background: Color = Color(0xA445AD47)
) {
    val context = LocalContext.current
    val fpsMonitor = FpsMonitor()
    val ramMonitor = RamMonitor(context)
    val networkUsageMonitor = NetworkUsageMonitor()

    var fps by remember { mutableStateOf(0) }
    var availableRam by remember { mutableStateOf(0) }
    var usedRam by remember { mutableStateOf(0) }

    var network by remember { mutableStateOf(NetworkParameters()) }

    LaunchedEffect(Unit) {
        fpsMonitor.launch { fps = it.toInt() }
        ramMonitor.launch { used, available ->
            availableRam = available.toInt()
            usedRam = used.toInt()
        }

        networkUsageMonitor.launch { totalReceivedBytes, totalSentBytes, downloadSpeedKb, uploadSpeedKb ->
            network = NetworkParameters(
                received = totalReceivedBytes,
                sent = totalSentBytes,
                downloadSpeed = downloadSpeedKb.round(),
                uploadSpeed = uploadSpeedKb.round()
            )
        }
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(background)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomText("GPU Monitoring", text = "FPS: $fps")
            CustomText("Ram Monitoring", text = "Used-Ram: $usedRam \nAvailable-Ram: $availableRam")
            CustomText(
                title = "Network Monitoring",
                text = "download-speed: ${network.downloadSpeed} KB/s \nuploadSpeed: ${network.uploadSpeed} KB/s\n\nReceived: ${network.received} Bytes \n" +
                        "Sent: ${network.sent} Bytes"
            )

        }
    }
}


@Composable
private fun CustomText(title: String, text: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
        )
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        )
    }

}

private data class NetworkParameters(
    val received: Long = 0,
    val sent: Long = 0,
    val downloadSpeed: Double = 0.0,
    val uploadSpeed: Double = 0.0,
)

private fun Double.round(): Double {
    return (this * 10).roundToInt().coerceIn(0, 100) / 10.0
}