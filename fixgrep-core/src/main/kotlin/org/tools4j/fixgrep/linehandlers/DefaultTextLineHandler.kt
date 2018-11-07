package org.tools4j.fixgrep.linehandlers

import mu.KLogging
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fixgrep.formatting.FormatSpec
import java.util.regex.Pattern

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:15 PM
 */
class DefaultTextLineHandler(val spec: FormatSpec, val fixLineHandler: FixLineHandler): LineHandler {
    companion object: KLogging()

    val logLineRegexPattern: Pattern by lazy {
        Pattern.compile(spec.lineRegex)
    }

    override fun finish() {
        fixLineHandler.finish()
    }

    override fun handle(line: String) {
        val fixLine = extractLine(line)
        if(fixLine != null){
            logger.debug { "handling line: $line" }
            fixLineHandler.handle(fixLine)
        }
    }

    private fun extractLine(line: String): FixLine? {
        val matcher = logLineRegexPattern.matcher(line)
        if(!matcher.find()){
            return null
        }
        val fixString = matcher.group(spec.lineRegexGroupForFix)
        val fields: Fields = FieldsFromDelimitedString(fixString, spec.inputDelimiter).fields
        return FixLine(fields, matcher)
    }
}