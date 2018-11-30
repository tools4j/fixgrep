package org.tools4j.fixgrep.highlights

import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor.*
import org.tools4j.fixgrep.texteffect.TextEffect
import org.tools4j.fixgrep.texteffect.TextEffectParser

class DefaultHighlightTextEffects(val effects: List<TextEffect>){
    var lastUsed = -1
    constructor(defaultHighlightTextEffects: DefaultHighlightTextEffects): this(defaultHighlightTextEffects.effects)
    constructor(str: String): this(TextEffectParser().parseToListOfTextEffects(str, ","))

    fun next(): TextEffect {
        return effects.get(++lastUsed % effects.size)
    }

    fun reset(){
        lastUsed = -1
    }

    companion object {
        val DEFAULT = DefaultHighlightTextEffects(listOf(Red, Green, Yellow, Blue, Purple, Cyan))

        fun toPrettyConsoleTextList():String{
            DEFAULT.reset()
            val sb = StringBuilder()
            DEFAULT.effects.forEach {
                if(sb.length > 0) sb.append(", ")
                sb.append(it.consoleTextBefore)
                sb.append(it.htmlClass)
                sb.append(it.consoleTextAfter)
            }
            return sb.toString()
        }

        fun toPrettyHtmlList():String{
            DEFAULT.reset()
            val sb = StringBuilder()
            DEFAULT.effects.forEach {
                if(sb.length > 0) sb.append(", ")
                sb.append("<span class='${it.htmlClass}'>${it.htmlClass}</span>")
            }
            return sb.toString()
        }
    }
}

