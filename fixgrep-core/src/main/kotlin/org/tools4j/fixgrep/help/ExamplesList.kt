package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.CompositeTextEffect
import org.tools4j.fixgrep.texteffect.ConsoleOnlyTextEffect
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.utils.WrappedFixGrep

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class ExamplesList (val fixLines: List<String>, val docWriter: DocWriter) {
    init {
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
    }

    fun add(args: String, description: String): ExamplesList {
        if(args == "<no arguments>") return add(emptyList(), description)
        else return add(args.split(" "), description)
    }

    fun add(args: List<String>, description: String): ExamplesList {
        val example = Example(description, args)
        docWriter.writeLn(example.description, CompositeTextEffect(ConsoleOnlyTextEffect.BlankLineBefore, HtmlOnlyTextEffect("example-description")))
        if(args.isEmpty()) docWriter.writeLn("<no arguments>", CompositeTextEffect(listOf(ConsoleOnlyTextEffect.BlankLineBefore, ConsoleOnlyTextEffect.BlankLineAfter, ConsoleOnlyTextEffect.Bold, HtmlOnlyTextEffect("example-arguments"))))
        else docWriter.writeLn(example.args.joinToString(" "), CompositeTextEffect(listOf(ConsoleOnlyTextEffect.BlankLineBefore, ConsoleOnlyTextEffect.BlankLineAfter, ConsoleOnlyTextEffect.Bold, HtmlOnlyTextEffect("example-arguments"))))

        val allArgs: MutableList<String> = ArrayList()
        if(docWriter.isHtml()) allArgs.add("--html");
        allArgs.add("--input-delimiter"); allArgs.add("|")
        allArgs.add("--output-format-horizontal-console"); allArgs.add("${'$'}{msgFix}")
        allArgs.addAll(args)

        val result = WrappedFixGrep(allArgs, false, false).go(fixLines.joinToString("\n"))

        docWriter.startSection(MiscTextEffect.Console)
        docWriter.write(result)
        docWriter.endSection()
        return this
    }

    fun end(){
        docWriter.endSection()
    }

    class Example(val description: String, val args: List<String>)
}