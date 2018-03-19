package com.doubleu.mute.app

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.Properties

object Properties {

    private val filename = "${System.getProperty("user.home")}\\MuteInBackground.properties"
    private val file = File(filename)
    private val path = file.toPath()
    private val props = Properties().apply {
        FileInputStream(file()).use {
            this.load(it)
        }
    }

    private const val pathToExecutableKey = "PathToExecutable"
    val pathToExecutableProperty = SimpleStringProperty(props[pathToExecutableKey] as String? ?: "")
            .apply {
                onChange { props[pathToExecutableKey] = it ?: "" }
            }
    var pathToExecutable by pathToExecutableProperty

    private const val mutedWindowsKey = "MutedWindows"
    val mutedWindowsProperty: SimpleStringProperty = SimpleStringProperty(props[mutedWindowsKey] as String? ?: "")
            .apply {
                onChange { props[mutedWindowsKey] = it ?: "" }
            }
    var mutedWindows by mutedWindowsProperty

    private const val startMinimizedKey = "StartMinimized"
    val startMinimizedProperty = SimpleBooleanProperty((props[startMinimizedKey] as? String)?.toBoolean() ?: false)
            .apply {
                onChange { props[startMinimizedKey] = it.toString() }
            }
    var startMinimized by startMinimizedProperty

    fun file(): File {
        if (Files.notExists(path)) {
            Files.createFile(path)
            Files.setAttribute(path, "dos:hidden", true)
        }
        return file
    }

    fun save() {
        FileOutputStream(file()).use {
            props.store(it, null)
        }
    }
}