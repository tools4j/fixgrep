package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.FormatSpec
import org.tools4j.fixgrep.highlights.DefaultHighlightTextEffects
import org.tools4j.fixgrep.highlights.HighlightExampleTable
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.TextEffect


/**
 * User: ben
 * Date: 16/05/2018
 * Time: 5:27 PM
 */
open class HtmlWriter(): DocWriter {
    val sb = StringBuilder()

    override fun startList(): DocWriter {
        write("<ul>\n")
        return this
    }

    override fun listItem(itemText: String): DocWriter {
        startListItem()
        write(itemText)
        endListItem()
        return this
    }

    override fun startListItem(): DocWriter {
        write("<li>")
        return this
    }

    override fun endListItem(): DocWriter {
        write("</li>\n")
        return this
    }

    override fun endList(): DocWriter {
        write("</ul>\n")
        return this
    }

    override fun isHtml(): Boolean {
        return true
    }

    override fun writeLink(linkText: String, url: String): DocWriter {
        write("<a href='$url'>$linkText</a>")
        return this
    }

    override fun writeLn(line: String, textEffect: TextEffect): HtmlWriter {
        writeDiv(line, textEffect.htmlClass)
        return this
    }

    override fun startSection(): DocWriter {
        write("<div>\n")
        return this
    }

    override fun startSection(textEffect: TextEffect): DocWriter {
        write("<div class='${textEffect.htmlClass}'>\n")
        return this
    }

    override fun endSection(): DocWriter {
        write("</div>\n")
        return this
    }

    override fun writeLn(): HtmlWriter {
        writeLn("")
        return this
    }

    override fun write(str: String, textEffect: TextEffect): HtmlWriter {
        write("<span class='${textEffect.htmlClass}'>$str</span>")
        return this
    }

    override fun writeListOfDefaultColors(): HtmlWriter {
        write(DefaultHighlightTextEffects.toPrettyHtmlList())
        return this
    }

    override fun writeListOfAnsi16ForegroundColors(): HtmlWriter {
        write(Ansi16ForegroundColor.listAsHtml())
        return this
    }

    override fun writeListOfAnsi16BackgroundColors(): HtmlWriter {
        write(Ansi16BackgroundColor.listAsHtml())
        return this
    }

    override fun writeFormatExamplesTable(fix: String): HighlightExampleTable {
        return HighlightExampleTable(fix, HtmlTableBuilder(this), FormatSpec().copyWithModifications(inputDelimiter = "|", outputDelimiter = "|", outputFormatHorizontalConsole = "${'$'}{msgFix}", formatInHtml = true))
    }

    override fun writeBoldLn(line: String): HtmlWriter {
        writeDiv(line, "bold")
        return this
    }

    override fun writeBoldLn(line: String, textEffect: TextEffect): HtmlWriter {
        writeDiv(line, "bold ${textEffect.htmlClass}")
        return this
    }

    override fun writeBold(str: String): HtmlWriter {
        writeSpan(str, "bold")
        return this
    }

    override fun writeBold(str: String, textEffect: TextEffect): HtmlWriter {
        writeSpan(str, "bold ${textEffect.htmlClass}")
        return this
    }

    override fun writeParagraph(content: String): HtmlWriter {
        writeDiv(content, "")
        return this
    }

    override fun writeLn(line: String): HtmlWriter {
        sb.append(line).append("<br/>")
        return this
    }

    override fun write(str: String): HtmlWriter {
        sb.append(str)
        return this
    }

    fun writeDiv(content: String, classes: String): HtmlWriter {
        write("<div class='$classes'>\n" )
        write(content)
        write("\n</div>\n")
        return this
    }

    fun writeSpan(content: String, classes: String): HtmlWriter {
        write("<span class='$classes'>\n" )
        write(content)
        write("\n</span>\n")
        return this
    }

    override fun writeHeading(level: Int, content: String): HtmlWriter {
        write("<h$level id='" + content.trim().replace(Regex("\\s"), "-") + "'>" )
        write(content)
        write("</h$level>\n")
        return this
    }

    override fun addTable(): TableBuilder {
        val tableBuilder = HtmlTableBuilder(this)
        return tableBuilder.startNewTable()
    }

    override fun addTable(textEffect: TextEffect): TableBuilder {
        val tableBuilder = HtmlTableBuilder(this, textEffect)
        return tableBuilder.startNewTable()
    }

    override fun toFormattedText(): String {
        return sb.toString()
    }

    override fun toString(): String {
        return toFormattedText()
    }
}