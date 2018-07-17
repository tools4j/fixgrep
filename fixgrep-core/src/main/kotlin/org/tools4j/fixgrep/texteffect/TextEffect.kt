package org.tools4j.fixgrep.texteffect

interface TextEffect {
    val consoleTextBefore: String
    val consoleTextAfter: String
    val htmlClass: String
    val name: String

    companion object {
        val NONE = TextEffectImpl("","")

        fun equals(textEffect1: TextEffect, textEffect2: TextEffect): Boolean{
            return textEffect1.consoleTextBefore == textEffect2.consoleTextBefore
                && textEffect1.consoleTextBefore == textEffect2.consoleTextBefore
                && textEffect1.consoleTextBefore == textEffect2.consoleTextBefore
                && textEffect1.consoleTextBefore == textEffect2.consoleTextBefore
        }
    }
}
