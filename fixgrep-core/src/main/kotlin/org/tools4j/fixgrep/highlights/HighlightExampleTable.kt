package org.tools4j.fixgrep.highlights

import org.tools4j.fixgrep.formatting.FormatSpec
import org.tools4j.fixgrep.formatting.WrappedFormatter
import org.tools4j.fixgrep.help.DocWriter
import org.tools4j.fixgrep.help.TableBuilder
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class HighlightExampleTable(val fix: String, val tableBuilder: TableBuilder, val spec: FormatSpec) {
    val examples: MutableList<String> = ArrayList()
    val highlightParser = HighlightParser()

    fun add(expression: String): HighlightExampleTable{
        examples.add(expression)
        return this
    }

    fun endTable(): DocWriter {
        tableBuilder.startNewTable(HtmlOnlyTextEffect("example-table"))
        tableBuilder.startNewRow().addTableHeaderCell("expression").addTableHeaderCell("message")
        for(expression in examples){
            val highlight = HighlightParser().parse(expression)
            val formatter = WrappedFormatter(spec.copyWithModifications(highlight = highlight))
            val formattedString = formatter.format(fix)
            tableBuilder.startNewRow().addCell("-h " + expression).addCell(formattedString!!, MiscTextEffect.Console)
        }
        return tableBuilder.endTable()
    }
}