package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 25/04/2018
 * Time: 6:21 AM
 */
class MiscTextEffect(ansiCode: String, override val name: String): TextEffectImpl(ansiCode) {
    override val htmlClass: String by lazy {
        name
    }

    companion object {
        val Bold = MiscTextEffect("\u001B[1m", "bold")

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