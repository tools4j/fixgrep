package org.tools4j.model.fix.messages;

import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.FieldsAnnotator
import org.tools4j.fix.FieldsSource
import org.tools4j.fix.NonAnnotatedSingleLineFormat
import org.tools4j.fix.spec.FixSpecDefinition

/**
 * User: ben
 * Date: 13/02/2018
 * Time: 6:05 PM
 */
abstract class FixMessage(val fixSpec: FixSpecDefinition): FieldsSource, Message {
    val consoleText: String by lazy {
        FieldsAnnotator(this.fields, fixSpec).fields.toDelimitedString("|")
    }

    fun toFix(): String {
        return NonAnnotatedSingleLineFormat(this, Ascii1Char().toString()).toString()
    }

    fun toConsoleText(): String{
        return consoleText
    }
}
