package com.doubleu.mute.model

import javafx.beans.property.*
import tornadofx.*

class Window(handle: String, lev: Int, pid: Long, status: WindowStatus, image: String, caption: String) {

    data class WindowStatus(var maximized: Boolean, var active: Boolean, var enabled: Boolean, var visible: Boolean)

    val captionProperty = SimpleStringProperty(caption)
    var caption by captionProperty
    val imageProperty = SimpleStringProperty(image)
    var image by imageProperty
    val statusProperty = SimpleObjectProperty<WindowStatus>(status)
    var status by statusProperty
    val pidProperty = SimpleLongProperty(pid)
    var pid by pidProperty
    val levProperty = SimpleIntegerProperty(lev)
    var lev by levProperty
    val handleProperty = SimpleStringProperty(handle)
    var handle by handleProperty
    val mutedProperty = SimpleBooleanProperty(false)
    var muted by mutedProperty

    val muteInBackground: Boolean
        get() = WindowStorage.muted.any { it.image == this.image }
}