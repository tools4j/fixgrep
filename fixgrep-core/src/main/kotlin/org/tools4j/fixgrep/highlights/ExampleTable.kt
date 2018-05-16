package org.tools4j.fixgrep.highlights

import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FieldsAnnotator
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class ExampleTable(val fix: String) {
    val examples: MutableList<String> = ArrayList()
    val highlightParser = HighlightParser()

    fun add(expression: String): ExampleTable{
        examples.add(expression)
        return this
    }

    fun toHtml(): String {
        val fixSpec = Fix50SP2FixSpecFromClassPath().spec
        val fields = FieldsFromDelimitedString(fix, "|").fields
        val annotatedFields = FieldsAnnotator(fields, fixSpec).fields
        val widestExpression = examples.stream().map{it.length}.max(Comparator.naturalOrder()).get()
        val sb = StringBuilder("<table class=\"example-table\">")
        sb.append("<tr><th>expression</th>").append("<th>message</th></tr>\n")
        for(expression in examples){
            sb.append(("<tr><td>-f " + expression)).append("</td><td>").append(highlightParser.parse(expression).apply(annotatedFields).toHtml()).append("</td></tr>\n")
        }
        sb.append("</table>")
        return sb.toString()
    }
}