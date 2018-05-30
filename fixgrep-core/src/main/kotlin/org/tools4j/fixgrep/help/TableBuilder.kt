package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 6:16 PM
 */
interface TableBuilder {
    fun startNewTable(): TableBuilder
    fun startNewTable(htmlOnlyTextEffect: HtmlOnlyTextEffect): TableBuilder
    fun endTable(): DocWriter
    fun startNewRow(): TableBuilder
    fun addTableHeaderCell(text: String): TableBuilder
    fun addCell(text: String): TableBuilder
    fun addCell(text: String, textEffect: TextEffect): TableBuilder
}
