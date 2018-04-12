package org.tools4j.model.fix.messages;

import org.tools4j.fix.FieldsNameAndEnumEnricher
import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.FieldsSource
import org.tools4j.fix.FixSpec
import org.tools4j.fix.NonAnnotatedSingleLineFormat

/**
 * User: ben
 * Date: 13/02/2018
 * Time: 6:05 PM
 */
abstract class FixMessage(val fixSpec: FixSpec): FieldsSource, Message {
    fun toFix(): String {
        return NonAnnotatedSingleLineFormat(this, Ascii1Char().toChar()).toString()
    }

    fun toAnnotatedFix(): String{
        return FieldsNameAndEnumEnricher(fixSpec, this.fields).toString()
    }
}
