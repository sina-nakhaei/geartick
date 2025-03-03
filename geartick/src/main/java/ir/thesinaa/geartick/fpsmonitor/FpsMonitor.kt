package ir.thesinaa.geartick.fpsmonitor

import android.view.Choreographer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FpsMonitor {
    private var frameCount = 0
    private var lastTime = System.nanoTime()
    private val choreographer = Choreographer.getInstance()
    private var monitoringJob: Job? = null

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            frameCount++
            choreographer.postFrameCallback(this)
        }
    }

    fun launch(
        context: CoroutineContext = Dispatchers.Main,
        updateIntervalMs: Long = 1000,
        onFpsUpdate: (fps: Double) -> Unit
    ) {
        frameCount = 0
        lastTime = System.nanoTime()
        choreographer.postFrameCallback(frameCallback)

        monitoringJob = CoroutineScope(context).launch {
            while (isActive) {
                delay(updateIntervalMs)
                val currentTime = System.nanoTime()
                val elapsedTime = (currentTime - lastTime).toSeconds()

                val fps = frameCount / elapsedTime
                onFpsUpdate.invoke(fps)
                frameCount = 0
                lastTime = currentTime
            }
        }
    }

    fun cancel() {
        choreographer.removeFrameCallback(frameCallback)
        monitoringJob?.cancel()
    }
}

private fun Long.toSeconds(): Double {
    return this / 1_000_000_000.0
}