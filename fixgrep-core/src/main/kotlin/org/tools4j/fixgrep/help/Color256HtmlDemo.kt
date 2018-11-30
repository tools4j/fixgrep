package org.tools4j.fixgrep.help

import mu.KotlinLogging
import org.tools4j.fixgrep.texteffect.*

/**
 * User: ben
 * Date: 9/05/2018
 * Time: 6:43 AM
 */
class Color256HtmlDemo(val docWriterFactory: DocWriterFactory) {
    fun toFormattedText(): String {
        val writer = docWriterFactory.createNew()
        if(!writer.isHtml()) return ""

        writer.writeLn("</br>If your console supports 256 colors, this demo should show a table like the one below:")
        val table = writer.addTable(HtmlOnlyTextEffect("color256-table"))
        table.startNewRow()
        for(i in 0..15){
            table.addCell(""+i, getTextEffectForColor(i))
        }
        for(i in 0..6){
            table.startNewRow()
            val colorPart1 = i*36 +16
            for(j in 0..35){
                val color = colorPart1+j
                if(color > 255) break
                table.addCell(""+color, getTextEffectForColor(color, (j>10)))
            }
        }
        table.endTable()
        return writer.toFormattedText()
    }

    private fun getTextEffectForColor(color: Int): TextEffect {
        return getTextEffectForColor(color, (color == 3 || color in 6..15))
    }

    private fun getTextEffectForColor(color: Int, darkOnLight: Boolean): TextEffect {
        val backgroundEffect = Ansi256Color(color, AnsiForegroundBackground.BACKGROUND)
        val foregroundEffect = if(darkOnLight) Ansi256Color(0, AnsiForegroundBackground.FOREGROUND) else Ansi256Color(15, AnsiForegroundBackground.FOREGROUND)
        val compositeTextEffect = CompositeTextEffect(linkedSetOf(backgroundEffect, foregroundEffect))
        return compositeTextEffect
    }

    companion object {
        val logger = KotlinLogging.logger {}
        @JvmStatic
        fun main(args: Array<String>) {
            val demo = Color256HtmlDemo(DocWriterFactory.Html)
            logger.info(demo.toFormattedText())
        }
    }
}