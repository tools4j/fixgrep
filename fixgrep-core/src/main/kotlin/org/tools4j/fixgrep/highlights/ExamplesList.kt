package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fixgrep.ConfigBuilder
import org.tools4j.fixgrep.FormatSpec
import org.tools4j.fixgrep.Formatter
import org.tools4j.fixgrep.help.DocWriter
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.properties.ConfigImpl
import java.util.function.Function

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class ExamplesList(val fixLines: List<String>, val docWriter: DocWriter) {
    val fixSpec = Fix50SP2FixSpecFromClassPath().spec

    init {
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
    }

    fun add(args: String, description: String): ExamplesList{
        return add(args.split(" "), description)
    }

    fun add(args: List<String>, description: String): ExamplesList{
        val example = Example(description, args)
        docWriter.writeBoldLn(example.args.joinToString(" "))
        docWriter.writeLn(example.description)
        val config = ConfigBuilder(example.args).config
        config.overrideWith(ConfigImpl("html", ""+docWriter.isHtml()))
        val spec = FormatSpec(config)
        spec.formatInHtml
        val formatter = Formatter(spec)
        docWriter.startSection(HtmlOnlyTextEffect.Console)
        for(line in fixLines){
            docWriter.writeLn(formatter.format(line) ?: "")
        }
        docWriter.endSection()
        return this
    }

    fun end(){
        docWriter.endSection()
    }

    class Example(val description: String, val args: List<String>)
}