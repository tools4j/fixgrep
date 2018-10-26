package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Delimiter
import org.tools4j.fix.DelimiterImpl
import org.tools4j.fix.FieldVisitor
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class HorizontalHtmlMsgFormatter(val context: FormattingContext, val delimiter: Delimiter) : MsgFormatter(), FieldWriter {
    constructor(formattingContext: FormattingContext, delimiter: String): this(formattingContext, DelimiterImpl(delimiter))

    val sb = StringBuilder()
    var writtenAtLeastOneField = false

    override fun getFieldVisitor(): FieldVisitor {
        return HorizontalHtmlFieldFormatter(this, context)
    }

    override fun writeField(value: String) {
        if(writtenAtLeastOneField){
            sb.append("<span class='delim'>")
            sb.append(delimiter.delimiter)
            sb.append("</span>")
        } else {
            sb.append("<div class='fields")
            if(msgTextEffect != TextEffect.NONE) sb.append(" ${msgTextEffect.htmlClass}")
            sb.append("'>")
        }
        sb.append(value)
        writtenAtLeastOneField = true
    }

    override fun format(): String{
        context.fields.accept(this)
        sb.append("</div>")
        return sb.toString()
    }
}