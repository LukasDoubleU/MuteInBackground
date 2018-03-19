package com.doubleu.mute.app

import com.doubleu.mute.cmd.Cmdow
import com.doubleu.mute.model.WindowStorage

object WindowWorker : Thread() {

    var run = true
    val sleepTime = 1000L
    private var running = false

    override fun run() {
        while (run && !running) {
            sleep(sleepTime)
            if (!run) break
            running = true
            val windows = Cmdow.windows()
            windows.forEach {
                WindowStorage.update(it)
            }
            WindowStorage.windows.removeIf { !windows.map { it.image }.contains(it.image) }
            running = false
        }
    }
}