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
class ExamplesList (val fixLines: List<String>, val docWriter: DocWriter) {
    val fixSpec = Fix50SP2FixSpecFromClassPath().spec

    init {
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
    }

    fun add(args: String, description: String): ExamplesList {
        if(args == "<no arguments>") return add(emptyList(), description)
        else return add(args.split(" "), description)
    }

    fun add(args: List<String>, description: String): ExamplesList {
        val example = Example(description, args)
        docWriter.writeLn(example.description, HtmlOnlyTextEffect("example-description"))
        if(args.isEmpty()) docWriter.writeBoldLn("<no arguments>", HtmlOnlyTextEffect("example-arguments"))
        else docWriter.writeBoldLn(example.args.joinToString(" "))

        val configOverrides: MutableMap<String, String> = LinkedHashMap()
        configOverrides.put("html", ""+docWriter.isHtml())
        configOverrides.put("input.delimiter", "|")
        configOverrides.put("output.line.format", "${'$'}{msgFix}")

        val config = ConfigBuilder(example.args, ConfigImpl(configOverrides)).config

        val spec = FormatSpec(config = config, fixSpec = fixSpec)
        val formatter = Formatter(spec)
        docWriter.startSection(MiscTextEffect.Console)
        for(line in fixLines){
            val formattedLine = formatter.format(line)
            if(formattedLine != null) docWriter.writeLn(formattedLine)
        }
        docWriter.endSection()
        return this
    }

    fun end(){
        docWriter.endSection()
    }

    class Example(val description: String, val args: List<String>)
}