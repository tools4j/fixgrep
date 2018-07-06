package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Delimiter
import org.tools4j.fix.DelimiterImpl
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.highlights.*
import java.util.stream.Collectors

/**
 * User: benjw
 * Date: 7/6/2018
 * Time: 6:44 AM
 */
class HorizontallyFormattedFields(val fields: Fields, val delimiter: Delimiter): FormattedFields, Fields by fields {
    constructor(fields: Fields, delimiter: String): this(fields, DelimiterImpl(delimiter))

    override fun highlight(criteria: HighlightCriteria, action: HighlightAction): HorizontallyFormattedFields {
        val outputDelimiter = if (action.scope == HighlightScope.Line){
            HighlightedDelimiter(delimiter.delimiter, action.textEffect)
        } else {
            delimiter
        }
        return HorizontallyFormattedFields(action.apply(fields, criteria), outputDelimiter)
    }

    override fun toConsoleText(): String {
        val sb = StringBuilder()
        for (i in 0 until fields.size) {
            if (i > 0) sb.append(outputDelimiter.toConsoleText())
            sb.append(fields[i].toConsoleText())
        }
        return sb.toString()
    }

    override fun toHtml(): String {
        val sb = StringBuilder("<span class='fields'>")
        for (i in 0 until fields.size) {
            if(i > 0) sb.append(outputDelimiter.toHtml())
            sb.append(fields[i].toHtml())
        }
        sb.append("</span>")
        return sb.toString()
    }
}