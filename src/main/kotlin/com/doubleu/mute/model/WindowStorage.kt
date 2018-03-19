package com.doubleu.mute.model

import com.doubleu.mute.app.Properties
import com.doubleu.mute.cmd.Cmdow
import com.doubleu.mute.cmd.Nircmd
import javafx.application.Platform
import tornadofx.*
import java.io.*
import java.util.*


object WindowStorage {
    val windows = Cmdow.windows().observable()
    val muted = (
            if (Properties.mutedWindows.isNullOrBlank())
                mutableListOf()
            else
                (fromString(Properties.mutedWindows) as Array<MutedWindow>).toMutableList())
            .observable().apply {
                onChange { Properties.mutedWindows = toString(this.toTypedArray()) }
            }

    fun find(pid: Long) = try {
        windows.find { it.pid == pid }
    } catch (e: ConcurrentModificationException) {
        null
    }

    fun update(window: Window) {
        val w = find(window.pid)
        if (w == null) Platform.runLater { windows.add(window) }
        else {
            w.status.active = window.status.active
            val active = w.status.active
            // we muted it previously but are no longer supposed to
            if (w.muted && !w.muteInBackground)
            // unmute it
                Nircmd.muteApp(w, false)
            // Are we observing it
            else if (w.muteInBackground) {
                // Inactive and not yet muted?
                if (!active && !w.muted)
                // mute it
                    Nircmd.muteApp(w, true)
                // Active but we muted it?
                else if (active && w.muted)
                // unmute
                    Nircmd.muteApp(w, false)
            }
        }
    }

    private fun fromString(s: String): Any {
        val data = Base64.getDecoder().decode(s)
        val ois = ObjectInputStream(ByteArrayInputStream(data))
        val o = ois.readObject()
        ois.close()
        return o
    }

    private fun toString(o: Serializable): String {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(o)
        oos.close()
        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }
}
