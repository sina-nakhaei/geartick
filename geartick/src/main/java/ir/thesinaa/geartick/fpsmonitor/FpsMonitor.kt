package ir.thesinaa.geartick.fpsmonitor

import android.view.Choreographer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FpsMonitor(
    private val intervalMillis: Long = INTERVAL_MILLIS
) {
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
        onFpsUpdate: (fps: Double) -> Unit
    ) {
        frameCount = 0
        lastTime = System.nanoTime()
        choreographer.postFrameCallback(frameCallback)

        monitoringJob = CoroutineScope(context).launch {
            while (isActive) {
                delay(intervalMillis)
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

    companion object {
        private const val INTERVAL_MILLIS = 1000L
    }
}

private fun Long.toSeconds(): Double {
    return this / 1_000_000_000.0
}