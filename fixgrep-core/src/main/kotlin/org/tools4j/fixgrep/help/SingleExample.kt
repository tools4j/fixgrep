package org.tools4j.fixgrep.help

import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fixgrep.*
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.properties.ConfigImpl

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class SingleExample (val fixLines: List<String>, val args: List<String>, val docWriterFactory: DocWriterFactory) {
    fun toFormattedString(): String {
        val docWriter = docWriterFactory.createNew()
        val configOverrides: MutableMap<String, String> = LinkedHashMap()
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
        configOverrides.put(Option.html.key, ""+docWriter.isHtml())
        configOverrides.put(Option.input_delimiter.key, "^A")
        val configAndArguments = ConfigBuilder(args, ConfigImpl(configOverrides)).configAndArguments
        val spec = FormatSpec(config = configAndArguments.config)
        val formatter = WrappedFormatter(spec)
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