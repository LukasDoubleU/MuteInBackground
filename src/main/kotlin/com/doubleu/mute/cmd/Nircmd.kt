package com.doubleu.mute.cmd

import com.doubleu.mute.model.Window
import java.nio.file.Files
import java.nio.file.Path

object Nircmd {
    private const val filename = "/cmd/nircmd.exe"
    val path: Path = temp()
        get() = if (Files.exists(field)) field else temp()

    private fun temp() = Files.write(Files.createTempFile("MuteInBackground_Nircmd", ".exe"), javaClass.getResourceAsStream(filename).readBytes()).toAbsolutePath()!!

    fun setAppVolume(pid: Long, vol: Int) {
        Runtime.getRuntime().exec("$path setappvolume /$pid $vol")
    }

    fun toggleMute(pid: Long) {
        Runtime.getRuntime().exec("$path muteappvolume /$pid 2")
    }

    fun muteApp(w: Window, mute: Boolean) {
        val muteId = if (mute) 1 else 0
        Runtime.getRuntime().exec("$path muteappvolume /${w.pid} $muteId")
        w.muted = mute
    }
}