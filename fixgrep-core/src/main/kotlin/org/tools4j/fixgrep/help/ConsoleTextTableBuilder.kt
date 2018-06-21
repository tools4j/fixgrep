package org.tools4j.fixgrep.help

import org.tools4j.extensions.lengthNotIncludingAnsiCodes
import org.tools4j.extensions.padStringContainingAnsiCodesEnd
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 6:19 PM
 */
class ConsoleTextTableBuilder(val writer: DocWriter): TableBuilder {
    val table = Table()
    var row: Row? = null

    override fun startNewTable(htmlOnlyTextEffect: HtmlOnlyTextEffect): ConsoleTextTableBuilder {
        //Nothing to do
        return this;
    }

    override fun startNewTable(): ConsoleTextTableBuilder {
        //Nothing to do
        return this
    }

    override fun endTable(): DocWriter {
        table.write(writer)
        return writer
    }

    override fun startNewRow(): ConsoleTextTableBuilder {
        table.rows.add(Row())
        return this
    }

    override fun addTableHeaderCell(text: String): ConsoleTextTableBuilder {
        table.rows.last().cells.add(Cell(text, MiscTextEffect.Bold))
        return this
    }

    override fun addCell(text: String): ConsoleTextTableBuilder {
        table.rows.last().cells.add(Cell(text, null))
        return this
    }

    override fun addCell(text: String, textEffect: TextEffect): ConsoleTextTableBuilder {
        table.rows.last().cells.add(Cell(text, textEffect))
        return this
    }

    class Table{
        val rows: MutableList<Row> = ArrayList()
        fun longestCellInColumn(colIndex: Int): Int {
            return rows.stream().map{it.cells.get(colIndex).text.lengthNotIncludingAnsiCodes()}.max(Comparator.naturalOrder()).get()
        }
        fun longestNumberOfColumns(): Int{
            return rows.stream().map{it.cells.size}.max(Comparator.naturalOrder()).get()
        }
        fun write(writer: DocWriter){
            val columnWidths: Array<Int?> = arrayOfNulls(longestNumberOfColumns())
            for(row in rows){
                for(i in 0 .. (row.cells.size - 1)){
                    //check to see if we have column width
                    if(columnWidths[i] == null) columnWidths[i] = longestCellInColumn(i) + 1
                    val columnWidth = columnWidths[i]
                    val cell = row.cells[i]
                    writer.write("|")
                          .write(cell.textEffectOrBlank)
                          .write(" ")
                          .write(cell.text.padStringContainingAnsiCodesEnd(columnWidth!!))
                          .write(cell.textEffectResetOrBlank)
                    if(i == (row.cells.size - 1)){
                        writer.write("|\n")
                    }
                }
            }
        }
    }

    class Row{
        val cells: MutableList<Cell> = ArrayList()
    }

    class Cell(val text: String, val textEffect: TextEffect?){
        val textEffectOrBlank: String by lazy {
           if(textEffect == null) ""
           else textEffect.consoleTextBefore
        }
        val textEffectResetOrBlank: String by lazy {
            if(textEffect == null) ""
            else textEffect.consoleTextAfter
        }
    }
}