package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field
import org.tools4j.fixgrep.texteffect.CompositeTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

class HighlightedField(val field: Field, val textEffectParam: TextEffect): Field by field {
    val textEffect: TextEffect by lazy {
        if(field is HighlightedField){
            CompositeTextEffect(listOf(field.textEffect, textEffectParam))
        } else {
            textEffectParam
        }
    }

    override fun toConsoleText(): String {
        val returnString = textEffect.consoleTextBefore + field.toConsoleText()
        return if(textEffect.consoleTextAfter == "" || returnString.endsWith(textEffect.consoleTextAfter))
                    returnString
                else
                    returnString + textEffect.consoleTextAfter

    }

    override fun toHtml(): String{
        return "<span class='field ${textEffect.htmlClass}'>" + tag.toHtml() + "<span class='equals'>=</span>" + value.toHtml() + "</span>"
    }
}