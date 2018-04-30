package org.tools4j.fixgrep.highlights

import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FieldsNameAndEnumEnricher
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

    override fun toString(): String {
        val fixSpec = Fix50SP2FixSpecFromClassPath().load()
        val fields = FieldsFromDelimitedString(fix, "|").fields
        val annotatedFields = FieldsNameAndEnumEnricher(fixSpec, fields).fields
        val widestExpression = examples.stream().map{it.length}.max(Comparator.naturalOrder()).get()
        val pad = (widestExpression + 6)
        val sb = StringBuilder()
        sb.append("| expression".padEnd(pad)).append("| message\n")
        for(expression in examples){
            sb.append(("| -f " + expression).padEnd(pad)).append("| ").append(highlightParser.parse(expression).apply(annotatedFields).toPrettyString(":")).append(" |\n")
        }
        return sb.toString()
    }
}