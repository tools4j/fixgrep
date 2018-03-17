package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import org.tools4j.fix.OutsideAnnotatedSingleLineFormat

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:32 PM
 */
class SimpleFieldsToStringProcessor(val delimiter: Char, val output: StringProcessor): FieldsProcessor {
    override fun accept(fields: Fields){
        output.accept(OutsideAnnotatedSingleLineFormat(fields, delimiter).stringValue)
    }
}