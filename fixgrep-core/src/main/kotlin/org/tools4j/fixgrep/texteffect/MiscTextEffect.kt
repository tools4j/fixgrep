package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 25/04/2018
 * Time: 6:21 AM
 */
class MiscTextEffect(ansiCode: String, override val name: String): TextEffectImpl(ansiCode) {
    override val prettyName: String by lazy {
        ansiCode + name + Ansi.Reset.ansiCode
    }

    companion object {
        val Bold = MiscTextEffect("\u001B[1m", "Bold")

        @JvmStatic
        fun contains(expression: String): Boolean {
            return expression.toLowerCase() == "bold"
        }

        @JvmStatic
        fun parse(expression: String): TextEffect {
            return Bold
        }
    }
}