package org.tools4j.fixgrep

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:12 AM
 */
interface Label {
    fun shouldLabel(originalFields: Fields, line:String): Boolean
    fun labelAndReturnNewLine(originalFields: Fields, line:String): String
}