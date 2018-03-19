package com.doubleu.mute.model

import java.io.Serializable

data class MutedWindow(var image: String, var caption: String) : Serializable {
    override fun equals(other: Any?): Boolean {
        return other is MutedWindow && image == other.image
    }

    override fun hashCode(): Int {
        var result = image.hashCode()
        result = 31 * result + caption.hashCode()
        return result
    }
}