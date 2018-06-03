package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 9/04/2018
 * Time: 9:40 AM
 */
open class TextEffectImpl(override val consoleTextBefore: String, override val consoleTextAfter: String) : TextEffect {
    constructor(consoleTextBefore: String): this(consoleTextBefore, Ansi.Normal)

    override val name: String by lazy {
        consoleTextBefore
    }

    override val htmlClass: String by lazy {
        consoleTextBefore.removePrefix("\u001b[")
    }

    companion object {
        @JvmStatic
        fun containsEscapeCode(str: String): Boolean{
            return str.startsWith("\u001b[")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextEffectImpl) return false

        if (consoleTextBefore != other.consoleTextBefore) return false

        return true
    }

    override fun hashCode(): Int {
        return consoleTextBefore.hashCode()
    }

    override fun toString(): String {
        return "RawColor(consoleTextBefore='$consoleTextBefore')"
    }
}