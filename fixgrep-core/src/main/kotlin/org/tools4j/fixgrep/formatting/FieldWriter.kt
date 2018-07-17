package org.tools4j.fixgrep.formatting

import org.tools4j.fix.FieldsVisitor

/**
 * User: benjw
 * Date: 7/17/2018
 * Time: 6:11 AM
 */
interface FieldWriter  {
    fun writeField(value: String);
}