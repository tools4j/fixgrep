package org.tools4j.fixgrep.texteffect

import java.util.*

interface TextEffect {
    val consoleTextBefore: String
    val consoleTextAfter: String
    val htmlClass: String
    fun contains(textEffect: TextEffect): Boolean

    fun compositeWith(other: TextEffect): TextEffect {
        return if(this == NONE) other
                else if(other == NONE) this
                else CompositeTextEffect(this, other)
    }

    companion object {
        val NONE = TextEffectImpl("","")

        fun equals(textEffect1: TextEffect, textEffect2: Any?): Boolean{
            if(textEffect2 == null || textEffect2 !is TextEffect) return false
            return textEffect1.consoleTextBefore == textEffect2.consoleTextBefore
                && textEffect1.consoleTextAfter == textEffect2.consoleTextAfter
                && textEffect1.htmlClass == textEffect2.htmlClass
        }

        fun hashCode(textEffect: TextEffect): Int{
            return Objects.hash(textEffect.consoleTextBefore, textEffect.consoleTextAfter, textEffect.htmlClass)
        }
    }
}
