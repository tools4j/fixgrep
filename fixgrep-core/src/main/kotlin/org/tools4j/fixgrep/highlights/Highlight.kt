package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Delimiter
import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 4/04/2018
 * Time: 5:29 PM
 */
interface Highlight {
    fun apply(fields: Fields): Fields;
    fun apply(delimiter: Delimiter): Delimiter;

    object NO_HIGHLIGHT: Highlight{
        override fun apply(delimiter: Delimiter): Delimiter {
            return delimiter
        }
        override fun apply(fields: Fields): Fields {
            return fields
        }
    }
}


