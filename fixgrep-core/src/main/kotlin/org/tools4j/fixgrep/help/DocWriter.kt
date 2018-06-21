package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.highlights.HighlightExampleTable
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 6:01 PM
 */
interface DocWriter {
    fun writeLn(line: String): DocWriter
    fun writeLn(line: String, textEffect: TextEffect): DocWriter
    fun writeLn(): DocWriter
    fun write(str: String): DocWriter
    fun write(str: String, textEffect: TextEffect): DocWriter
    fun writeParagraph(content: String): DocWriter
    fun writeHeading(level: Int, content: String): DocWriter
    fun writeBoldLn(line: String): DocWriter
    fun writeBoldLn(line: String, textEffect: TextEffect): DocWriter
    fun writeBold(str: String): DocWriter
    fun writeBold(str: String, textEffect: TextEffect): DocWriter
    fun addTable(): TableBuilder
    fun addTable(textEffect: TextEffect): TableBuilder
    fun writeListOfDefaultColors(): DocWriter
    fun toFormattedText(): String
    fun writeListOfAnsi16ForegroundColors(): DocWriter
    fun writeListOfAnsi16BackgroundColors(): DocWriter
    fun writeFormatExamplesTable(fix: String): HighlightExampleTable
    fun startSection(): DocWriter
    fun startSection(textEffect: TextEffect): DocWriter
    fun endSection(): DocWriter
    fun writeLink(linkText: String, url: String): DocWriter
    fun isHtml(): Boolean
    fun startList(): DocWriter
    fun listItem(itemText: String): DocWriter
    fun startListItem(): DocWriter
    fun endListItem(): DocWriter
    fun endList(): DocWriter
}