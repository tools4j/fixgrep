package org.tools4j.fixgrep.help

import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fixgrep.ConfigBuilder
import org.tools4j.fixgrep.FormatSpec
import org.tools4j.fixgrep.Formatter
import org.tools4j.fixgrep.Option
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.properties.ConfigImpl

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class SingleExample (val fixLines: List<String>, val args: List<String>, val docWriterFactory: DocWriterFactory) {
    fun toFormattedString(vertical: Boolean, verticallyAligned: Boolean): String {
        val docWriter = docWriterFactory.createNew()
        val configOverrides: MutableMap<String, String> = LinkedHashMap()
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
        configOverrides.put(Option.html.key, ""+docWriter.isHtml())
        configOverrides.put(Option.input_delimiter.key, "^A")
        if(vertical) configOverrides.put(Option.vertical_format.key, "true")
        if(verticallyAligned) configOverrides.put(Option.align_vertical_columns.key, "true")
        val configAndArguments = ConfigBuilder(args, ConfigImpl(configOverrides)).configAndArguments
        val spec = FormatSpec(config = configAndArguments.config)
        val formatter = Formatter(spec)
        docWriter.startSection(MiscTextEffect.Console)
        for(line in fixLines){
            val formattedLine = formatter.format(line)
            if(formattedLine != null) docWriter.write(formattedLine + "\n")
        }
        docWriter.endSection()
        docWriter.endSection()
        return docWriter.toFormattedText()
    }
}