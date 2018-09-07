package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class HorizontalConsoleMsgFormatter(val context: FormattingContext, val delimiter: Delimiter) : FieldWriter, MsgFormatter() {
    constructor(formattingContext: FormattingContext, delimiter: String): this(formattingContext, DelimiterImpl(delimiter))

    val sb = StringBuilder()

    override fun getFieldVisitor(): FieldVisitor {
        return HorizontalConsoleFieldFormatter(this, context, msgTextEffect)
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
        context.fields.accept(this)
        return sb.toString()
    }
}