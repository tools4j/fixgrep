package org.tools4j.fixgrep

import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixSpec
import java.io.InputStream
import java.util.function.Consumer

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrep(val inputStream: InputStream, val properties: Properties) {

    val lineProcessor: Consumer<String>

    init{
        val fixSpec = Fix50SP2FixSpecFromClassPath().load()
        val systemOutLineProcessor = SystemOutLineProcessor()
        val fieldsToStringToFieldsProcessor = SimpleFieldsToStringProcessor('|', systemOutLineProcessor)
        val fieldOrderingProcessor = FieldOrderingProcessor(listOf(35,55), fieldsToStringToFieldsProcessor)
        val annotatingFieldProcessor = AnnotatingFieldProcessor(fixSpec, fieldOrderingProcessor)
        val stringToFieldsProcessor = StringToFieldsProcessor(RegexPrefixFixStringFromLineExtractor(properties.fixExtractionRegex!!), annotatingFieldProcessor)
        lineProcessor = stringToFieldsProcessor
    }

    private fun go() {
        inputStream.bufferedReader().useLines { lines -> lines.forEach { handleLine(it) } }
    }

    private fun handleLine(line: String) {
        if (shouldProcessLine(line)) {
            println(lineProcessor.accept(line))
        }
    }

    private fun shouldProcessLine(line: String): Boolean {
        if (properties.fixPrefixRegex != null && line.contains(properties.fixPrefixRegex)) {
            return true
        } else if (properties.excludeRegex != null && line.contains(properties.excludeRegex)) {
            return false
        } else return properties.includeRegex != null && line.contains(properties.includeRegex)
    }
}