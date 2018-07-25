package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*
import org.tools4j.fixgrep.highlights.HighlightedFields
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class HorizontalConsoleMsgFormatter(val fields: Fields, val annotationPositions: AnnotationPositions, val boldTagAndValues: Boolean, val delimiter: Delimiter) : FieldWriter, MsgFormatter, FieldsVisitor {
    constructor(fields: Fields, annotationPositions: AnnotationPositions, boldTagAndValues: Boolean, delimiter: String): this(fields, annotationPositions, boldTagAndValues, DelimiterImpl(delimiter))

    var msgTextEffect: TextEffect = TextEffect.NONE
    val sb = StringBuilder()

    override fun getFieldVisitor(): FieldVisitor {
        return HorizontalConsoleFieldFormatter(this, annotationPositions, msgTextEffect, boldTagAndValues)
    }

    override fun visit(fields: Fields) {
        if(fields is HighlightedFields){
            msgTextEffect = fields.textEffect
        }
    }

    override fun writeField(value: String) {
        if(sb.length > 0){
            appendDelimiter()
        }
        sb.append(value)
    }

    private fun appendDelimiter() {
        sb.append(msgTextEffect.consoleTextBefore)
        sb.append(delimiter.delimiter)
        sb.append(msgTextEffect.consoleTextAfter)
    }

    override fun format(): String{
        fields.accept(this)
        return sb.toString()
    }
}