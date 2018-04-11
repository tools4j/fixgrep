package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field
import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.texteffect.Ansi16Color
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.TextEffect

class HighlightedField(val field: Field, val textEffect: TextEffect): Field by field {
    override fun toPrettyString(): String {
        val returnString = textEffect.ansiCode + field.toPrettyString()
        return if(returnString.endsWith(Ansi.Reset.ansiCode))
                    returnString
                else
                    returnString + Ansi.Reset.ansiCode

    }
}