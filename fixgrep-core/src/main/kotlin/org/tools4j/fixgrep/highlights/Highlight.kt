package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 4/04/2018
 * Time: 5:29 PM
 */
interface Highlight {
    fun apply(fields: Fields): Fields;

    object NO_HIGHLIGHT: Highlight{
        override fun apply(fields: Fields): Fields {
            return fields
        }
    }
}


