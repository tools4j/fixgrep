package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.FieldsFromDelimitedString
import java.util.function.Consumer

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:32 PM
 */
class StringToFieldsProcessor(val fixStringFromLineExtractor: FixStringFromLineExtractor, val output: FieldsProcessor, val delimiter: Char = Ascii1Char().toChar()): Consumer<String> {
    override fun accept(line: String){
        output.accept(FieldsFromDelimitedString(fixStringFromLineExtractor.createLine(line), delimiter).fields)
    }
}