package org.tools4j.fixgrep.texteffect

import javax.naming.OperationNotSupportedException

/**
 * User: benjw
 * Date: 26/11/2018
 * Time: 17:47
 */
class ConsoleOnlyTextEffect(override val consoleTextBefore: String, override val consoleTextAfter: String): TextEffect {
    override val htmlClass: String
        get() = ""

    override fun contains(textEffect: TextEffect): Boolean {
        return this.equals(textEffect)
    }

    override fun equals(other: Any?): Boolean {
        return TextEffect.equals(this, other)
    }

    override fun hashCode(): Int {
        return TextEffect.hashCode(this)
    }

    companion object {
        val BlankLineAfter = ConsoleOnlyTextEffect("", "\n")
        val BlankLineBefore = ConsoleOnlyTextEffect("\n", "")
        val Bold = ConsoleOnlyTextEffect("\u001B[1m", "\u001B[22m")
    }
}