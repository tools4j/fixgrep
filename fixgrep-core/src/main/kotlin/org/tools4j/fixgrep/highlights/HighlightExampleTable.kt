package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsAnnotator
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fixgrep.help.DocWriter
import org.tools4j.fixgrep.help.TableBuilder
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import java.util.function.Function

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class HighlightExampleTable(val fix: String, val tableBuilder: TableBuilder, val fieldsTransform: Function<in Fields, out String>) {
    val examples: MutableList<String> = ArrayList()
    val highlightParser = HighlightParser()

    fun add(expression: String): HighlightExampleTable{
        examples.add(expression)
        return this
    }

    fun endTable(): DocWriter {
        val fixSpec = Fix50SP2FixSpecFromClassPath().spec
        val fields = FieldsFromDelimitedString(fix, "|").fields
        val annotatedFields = FieldsAnnotator(fields, fixSpec).fields

        tableBuilder.startNewTable(HtmlOnlyTextEffect("example-table"))
        tableBuilder.startNewRow().addTableHeaderCell("expression").addTableHeaderCell("message")
        for(expression in examples){
            val formattedFields = highlightParser.parse(expression).apply(annotatedFields)
            tableBuilder.startNewRow().addCell("-f " + expression).addCell(fieldsTransform.apply(formattedFields), MiscTextEffect.Console)
        }
        return tableBuilder.endTable()
    }
}