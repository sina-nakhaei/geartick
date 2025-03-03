package ir.thesinaa.geartick.rammonitor

import android.app.ActivityManager
import android.content.Context
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RamMonitor(context: Context) {
    private var monitoringJob: Job? = null
    private val activityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    private val memoryInfo = ActivityManager.MemoryInfo()

    init { activityManager.getMemoryInfo(memoryInfo) }

    fun launch(
        context: CoroutineContext = Dispatchers.IO,
        updateIntervalMs: Long = 1000,
        onRamUpdate: (used: Long, available: Long) -> Unit
    ) {
        monitoringJob = CoroutineScope(context).launch {
            while (isActive) {
                withContext(Dispatchers.Main) {
                    val totalRam = memoryInfo.totalMem.toMegaByte()
                    val available = memoryInfo.availMem.toMegaByte()
                    val used = totalRam - available

                    onRamUpdate(used, available)
                }

                delay(updateIntervalMs)
            }
        }
    }

    fun stop() {
        monitoringJob?.cancel()
    }
}

private fun Long.toMegaByte(): Long {
    return this / (1024 * 1024)
}