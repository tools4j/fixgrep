package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 9/04/2018
 * Time: 9:40 AM
 */
open class TextEffectImpl(override val ansiCode: String) : TextEffect {
    override val prettyName: String by lazy {
        ansiCode + "\\" + ansiCode + Ansi.Reset.ansiCode
    }

    companion object {
        @JvmStatic
        fun contains(str: String): Boolean{
            return str.startsWith("\u001b[")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextEffectImpl) return false

        if (ansiCode != other.ansiCode) return false

        return true
    }

    override fun hashCode(): Int {
        return ansiCode.hashCode()
    }

    override fun toString(): String {
        return "RawColor(ansiCode='$ansiCode')"
    }
}