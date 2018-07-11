package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Delimiter
import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 4/04/2018
 * Time: 5:29 PM
 */
interface Highlight {
    fun applyToFields(fields: Fields): Fields;
    fun applyToDelimiter(fields: Fields, delimiter: Delimiter): Delimiter;

    object NO_HIGHLIGHT: Highlight{
        override fun applyToDelimiter(fields: Fields, delimiter: Delimiter): Delimiter {
            return delimiter
        }
        override fun applyToFields(fields: Fields): Fields {
            return fields
        }
    }
}


