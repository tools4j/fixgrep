package org.tools4j.fixgrep.help

import org.tools4j.fix.Ansi
import org.tools4j.fixgrep.highlights.DefaultHighlightTextEffects
import org.tools4j.fixgrep.highlights.HighlightExampleTable
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.*
import java.util.function.Function

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 6:05 PM
 */
class ConsoleTextWriter(): DocWriter {
    val sectionTextEffectsStack = LinkedList<TextEffect>()

    override fun startList(): DocWriter {
        return this
    }

    override fun listItem(itemText: String): DocWriter {
        startListItem()
        writeLn(itemText)
        endListItem()
        return this
    }

    override fun startListItem(): DocWriter {
        write("* ")
        return this
    }

    override fun endListItem(): DocWriter {
        return this
    }

    override fun endList(): DocWriter {
        return this
    }

    override fun writeBold(str: String, textEffect: TextEffect): DocWriter {
        write(textEffect.consoleTextBefore).writeBold(str).write(textEffect.consoleTextAfter)
        return this
    }

    override fun writeBoldLn(line: String, textEffect: TextEffect): DocWriter {
        write(textEffect.consoleTextBefore).writeBoldLn(line).write(textEffect.consoleTextAfter)
        return this
    }

    override fun writeLink(linkText: String, url: String): DocWriter {
        write(linkText).write(":").write(url).write(" ")
        return this;
    }

    override fun isHtml(): Boolean {
        return false
    }

    val sb = StringBuilder()

    override fun startSection(): DocWriter {
        sectionTextEffectsStack.push(MiscTextEffect.Null)
        return this
    }


    override fun startSection(textEffect: TextEffect): DocWriter {
        sectionTextEffectsStack.push(textEffect)
        write(textEffect.consoleTextBefore)
        return this
    }

    override fun endSection(): DocWriter {
        write(sectionTextEffectsStack.pop().consoleTextAfter)
        writeLn()
        return this
    }

    override fun writeLn(line: String, textEffect: TextEffect): DocWriter {
        write(textEffect.consoleTextBefore).writeLn(line).write(textEffect.consoleTextAfter)
        return this
    }

    override fun write(str: String, textEffect: TextEffect): ConsoleTextWriter {
        write(textEffect.consoleTextBefore).write(str).write(textEffect.consoleTextAfter)
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

    override fun addTable(textEffect: TextEffect): TableBuilder {
        return addTable()
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
            writeBoldLn("========================================================================================================")
            writeBoldLn(content.toUpperCase())
            writeBoldLn("========================================================================================================")
        } else if(level == 2) {
            writeBoldLn(content)
        } else {
            writeBoldLn(content)
        }
        return this
    }

    override fun writeBoldLn(line: String): ConsoleTextWriter {
        writeLn(Ansi.Bold + line + Ansi.Reset)
        return this
    }

    override fun writeBold(str: String): ConsoleTextWriter {
        write(Ansi.Bold + str + Ansi.Reset)
        return this
    }

    override fun toFormattedText(): String {
        return sb.toString()
    }

    override fun toString(): String {
        return toFormattedText()
    }
}