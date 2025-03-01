package ir.thesinaa.geartick.networkmonitor

import android.net.TrafficStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class NetworkUsageMonitor {
    private var monitoringJob: Job? = null

    fun launch(
        context: CoroutineContext = Dispatchers.IO,
        updateIntervalMs: Long,
        callback: (
            totalReceivedBytes: Long,
            totalSentBytes: Long,
            downloadSpeedKb: Double,
            uploadSpeedKb: Double
        ) -> Unit
    ) {
        monitoringJob = CoroutineScope(context).launch {
            var prevRxBytes = TrafficStats.getTotalRxBytes()
            var prevTxBytes = TrafficStats.getTotalTxBytes()

            while (isActive) {
                delay(updateIntervalMs)

                val currentRxBytes = TrafficStats.getTotalRxBytes()
                val currentTxBytes = TrafficStats.getTotalTxBytes()

                val rxSpeed = (currentRxBytes - prevRxBytes) / (updateIntervalMs / 1000)
                val txSpeed = (currentTxBytes - prevTxBytes) / (updateIntervalMs / 1000)

                prevRxBytes = currentRxBytes
                prevTxBytes = currentTxBytes

                withContext(Dispatchers.Main) {
                    callback(currentRxBytes, currentTxBytes, rxSpeed.toKb(), txSpeed.toKb())
                }
            }
        }
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
    }
}

private fun Long.toKb(): Double =
    this.toDouble() / 1024.0
