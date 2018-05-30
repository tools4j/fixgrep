package org.tools4j.fixgrep.help

import org.tools4j.fix.Ansi
import org.tools4j.fixgrep.highlights.DefaultHighlightTextEffects
import org.tools4j.fixgrep.highlights.HighlightExampleTable
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.function.Function

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 6:05 PM
 */
class ConsoleTextWriter(): DocWriter {
    override fun writeLink(linkText: String, url: String): DocWriter {
        write(linkText).write(":").write(url).write(" ")
        return this;
    }

    override fun isHtml(): Boolean {
        return false
    }

    val sb = StringBuilder()

    override fun startSection(): DocWriter {
        //No-op
        return this
    }

    override fun startSection(htmlOnlyTextEffect: HtmlOnlyTextEffect): DocWriter {
        //No-op
        return this
    }

    override fun endSection(): DocWriter {
        //No-op
        return this
    }

    override fun writeLn(line: String, textEffect: TextEffect): DocWriter {
        write(textEffect.ansiCode).write(line).writeLn(textEffect.ansiResetCode)
        return this
    }

    override fun write(str: String, textEffect: TextEffect): ConsoleTextWriter {
        write(textEffect.ansiCode).write(str).write(textEffect.ansiResetCode)
        return this
    }

    override fun writeListOfDefaultColors(): ConsoleTextWriter {
        write(DefaultHighlightTextEffects.toPrettyConsoleTextList())
        return this
    }

    override fun writeListOfAnsi16ForegroundColors(): ConsoleTextWriter {
        write(Ansi16ForegroundColor.listForConsole())
        return this
    }

    override fun writeListOfAnsi16BackgroundColors(): ConsoleTextWriter {
        write(Ansi16BackgroundColor.listForConsole())
        return this
    }

    override fun writeFormatExamplesTable(fix: String): HighlightExampleTable {
        return HighlightExampleTable(fix, addTable(), Function {it.toConsoleText()})
    }

    override fun addTable(): TableBuilder {
        val tableBuilder = ConsoleTextTableBuilder(this)
        return tableBuilder
    }

    override fun writeLn(): DocWriter {
        return writeLn("")
    }


    override fun writeLn(line: String): ConsoleTextWriter {
        sb.append(line)
        sb.append("\n")
        return this
    }

    override fun write(str: String): ConsoleTextWriter {
        sb.append(str)
        return this
    }

    override fun writeParagraph(content: String): ConsoleTextWriter {
        writeLn(content)
        writeLn("")
        return this
    }

    override fun writeHeading(level: Int, content: String): ConsoleTextWriter {
        if(level == 1){
            writeBoldLn(content.toUpperCase())
        } else {
            writeBoldLn(content)
        }
        return this
    }

    override fun writeBoldLn(line: String): ConsoleTextWriter {
        writeLn(Ansi.Bold + line + Ansi.Normal)
        return this
    }

    override fun writeBold(str: String): ConsoleTextWriter {
        write(Ansi.Bold + str + Ansi.Normal)
        return this
    }

    override fun toFormattedText(): String {
        return sb.toString()
    }

    override fun toString(): String {
        return toFormattedText()
    }
}