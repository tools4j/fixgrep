package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.highlights.DefaultHighlightTextEffects
import org.tools4j.fixgrep.highlights.HighlightExampleTable
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.function.Function


/**
 * User: ben
 * Date: 16/05/2018
 * Time: 5:27 PM
 */
open class HtmlWriter(): DocWriter {
    val sb = StringBuilder()

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
        writeLn("<div>")
        return this
    }

    override fun startSection(textEffect: TextEffect): DocWriter {
        writeLn("<div class='${textEffect.htmlClass}'>")
        return this
    }

    override fun endSection(): DocWriter {
        writeLn("</div>")
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
        return HighlightExampleTable(fix, HtmlTableBuilder(this), Function {it.toHtml()})
    }

    override fun writeBoldLn(line: String): HtmlWriter {
        writeDiv("bold", line)
        return this
    }

    override fun writeBold(str: String): HtmlWriter {
        writeSpan("bold", str)
        return this
    }

    override fun writeParagraph(content: String): HtmlWriter {
        writeDiv("", content)
        return this
    }

    override fun writeLn(line: String): HtmlWriter {
        sb.append(line)
        sb.append("<br/>\n")
        return this
    }

    override fun write(str: String): HtmlWriter {
        sb.append(str)
        return this
    }

    fun writeDiv(classes: String, content: String): HtmlWriter {
        writeLn("<div class='$classes'>\n" )
        writeLn(content)
        writeLn("\n</div>\n")
        return this
    }

    fun writeSpan(classes: String, content: String): HtmlWriter {
        write("<span class='$classes'>\n" )
        write(content)
        write("\n</span>\n")
        return this
    }

    override fun writeHeading(level: Int, content: String): HtmlWriter {
        write("<h$level>" )
        write(content)
        write("</h$level>\n")
        return this
    }

    override fun addTable(): TableBuilder {
        val tableBuilder = HtmlTableBuilder(this)
        return tableBuilder.startNewTable()
    }

    override fun toFormattedText(): String {
        return sb.toString()
    }

    override fun toString(): String {
        return toFormattedText()
    }
}