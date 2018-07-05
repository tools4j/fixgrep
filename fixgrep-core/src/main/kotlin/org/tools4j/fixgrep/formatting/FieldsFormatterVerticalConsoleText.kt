package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Delimiter
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFormatter

/**
 * User: benjw
 * Date: 7/5/2018
 * Time: 5:43 PM
 */
class FieldsFormatterVerticalConsoleText(): FieldsFormatter {
    override fun toFormattedText(fields: Fields): String {
        val sb = StringBuilder()
        for (i in 0 until fields.size) {
            if (i > 0) sb.append("\n")
            sb.append(fields[i].toConsoleText())
        }
        return sb.toString()
    }
}