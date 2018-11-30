package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 9/04/2018
 * Time: 9:40 AM
 */
open class TextEffectImpl(override val consoleTextBefore: String, override val consoleTextAfter: String) : TextEffect {
    constructor(consoleTextBefore: String): this(consoleTextBefore, Ansi.Reset)

    override fun contains(textEffect: TextEffect): Boolean {
        return this.equals(textEffect)
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

    override fun toString(): String {
        return "RawColor(consoleTextBefore='$consoleTextBefore')"
    }

    override fun equals(other: Any?): Boolean {
        return TextEffect.equals(this, other)
    }

    override fun hashCode(): Int {
        return TextEffect.hashCode(this)
    }
}