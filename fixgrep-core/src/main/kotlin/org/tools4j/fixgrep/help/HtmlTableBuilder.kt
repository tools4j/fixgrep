package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 6:19 PM
 */
class HtmlTableBuilder(val writer: DocWriter, val textEffect: TextEffect? = null): TableBuilder {
    var atLeastOneRowStarted = false

    override fun startNewTable(): HtmlTableBuilder {
        val tableClass = textEffect?.htmlClass ?:"doc-table"
        writer.write("<table class='$tableClass'>\n")
        return this
    }

    override fun startNewTable(htmlOnlyTextEffect: HtmlOnlyTextEffect): HtmlTableBuilder {
        writer.write("<table class='doc-table ${htmlOnlyTextEffect.htmlClass}'>\n")
        return this
    }

    override fun endTable(): DocWriter {
        if(atLeastOneRowStarted) writer.write("</tr>\n")
        writer.write("</table>\n")
        return writer
    }

    override fun startNewRow(): HtmlTableBuilder {
        if(atLeastOneRowStarted) writer.write("</tr>\n")
        writer.write("<tr>")
        atLeastOneRowStarted = true
        return this
    }

    override fun addTableHeaderCell(text: String): HtmlTableBuilder {
        writer.write("<th>$text</th>")
        return this
    }

    override fun addCell(text: String): HtmlTableBuilder {
        writer.write("<td>$text</td>")
        return this
    }

    override fun addCell(text: String, textEffect: TextEffect): HtmlTableBuilder {
        writer.write("<td class='${textEffect.htmlClass}'>$text</td>")
        return this
    }
}