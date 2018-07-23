package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 25/04/2018
 * Time: 6:21 AM
 */
class MiscTextEffect(consoleTextBefore: String, consoleTextAfter: String, override val htmlClass: String): TextEffectImpl(consoleTextBefore, consoleTextAfter) {
    constructor(consoleTextBefore: String, name: String): this(consoleTextBefore, Ansi.Reset, name)

    companion object {
        val Bold = MiscTextEffect("\u001B[1m", "\u001B[22m", "bold")

        val Null = MiscTextEffect("", "", "")

        val Console = MiscTextEffect(
                "",
                "",
                "console")

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