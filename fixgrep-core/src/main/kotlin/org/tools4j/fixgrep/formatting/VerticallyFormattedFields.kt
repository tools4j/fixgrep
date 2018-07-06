package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.highlights.*
import java.util.stream.Collectors

/**
 * User: benjw
 * Date: 7/6/2018
 * Time: 6:44 AM
 */
class VerticallyFormattedFields(val fields: Fields): FormattedFields, Fields by fields {
    override fun highlight(criteria: HighlightCriteria, action: HighlightAction): VerticallyFormattedFields {
        return VerticallyFormattedFields(action.apply(fields, criteria))
    }

    override fun toConsoleText(): String {
        val sb = StringBuilder()
        for (i in 0 until fields.size) {
            if (i > 0) sb.append("\n")
            sb.append(fields[i].toConsoleText())
        }
        return sb.toString()
    }

    override fun toHtml(): String {
        val sb = StringBuilder("<span class='fields'>")
        for (i in 0 until fields.size) {
            if(i > 0) sb.append("</br>")
            sb.append(fields[i].toHtml())
        }
        sb.append("</span>")
        return sb.toString()
    }
}