package org.tools4j.fixgrep.help

import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fixgrep.ConfigBuilder
import org.tools4j.fixgrep.FormatSpec
import org.tools4j.fixgrep.Formatter
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.properties.ConfigImpl

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class SingleExample (val fixLines: List<String>, val args: List<String>, val docWriterFactory: DocWriterFactory) {
    val fixSpec = Fix50SP2FixSpecFromClassPath().spec

    val docWriter: DocWriter by lazy { docWriterFactory.createNew() }

    init {
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
    }

    fun toFormattedString(): String {
        val configOverrides: MutableMap<String, String> = LinkedHashMap()
        configOverrides.put("html", ""+docWriter.isHtml())
        configOverrides.put("input.delimiter", "^A")
        configOverrides.put("output.line.format", "${'$'}{msgColor}${'$'}{msgTypeName}${'$'}{colorReset}:${'$'}{msgFix}")
        val configAndArguments = ConfigBuilder(args, ConfigImpl(configOverrides)).configAndArguments
        val spec = FormatSpec(config = configAndArguments.config, fixSpec = fixSpec)
        val formatter = Formatter(spec)
        docWriter.startSection(MiscTextEffect.Console)
        for(line in fixLines){
            val formattedLine = formatter.format(line)
            if(formattedLine != null) docWriter.write(formattedLine + "\n")
        }
        docWriter.endSection()
        return docWriter.toFormattedText()
    }
}