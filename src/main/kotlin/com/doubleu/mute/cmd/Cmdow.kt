package com.doubleu.mute.cmd

import com.doubleu.mute.model.Window
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors


object Cmdow {
    private const val filename = "/cmd/cmdow.exe"
    val path: Path = temp()
        get() = if (Files.exists(field)) field else temp()

    private fun temp() = Files.write(Files.createTempFile("MuteInBackground_Cmdow", ".exe"), javaClass.getResourceAsStream(filename).readBytes()).toAbsolutePath()!!

    fun windows(): List<Window> {
        val p = Runtime.getRuntime().exec("$path /T")
        val stdInput = BufferedReader(InputStreamReader(p.inputStream))
        return stdInput.lines().skip(1).filter { it != null && !it.isBlank() }.map { lineToApp(it) }.collect(Collectors.toList())
    }

    fun lineToApp(s: String): Window {
        val split = s.split("""\s+""".toRegex())
        return Window(split[0], split[1].toInt(), split[2].toLong(), toStatus(split[3], split[4], split[5], split[6]), split[7], s.substringAfter(split[7]).trim())
    }

    fun toStatus(max: String, act: String, ena: String, vis: String) = Window.WindowStatus(max == "Max", act == "Act", ena == "Ena", vis == "Vis")
}