package org.tools4j.fixgrep.formatting

import org.tools4j.fix.FieldVisitor
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class VerticalAlignedHtmlMsgFormatter(val context: FormattingContext) : MsgFormatter(), FieldWriter{
    val sb = StringBuilder()
    var writtenAtLeastOneField = false

    override fun getFieldVisitor(): FieldVisitor {
        return VerticalAlignedHtmlFieldFormatter(this, context)
    }

    override fun writeField(value: String) {
        if(!writtenAtLeastOneField){
            sb.append("<table class='fields")
            if(msgTextEffect != TextEffect.NONE) sb.append(" ${msgTextEffect.htmlClass}")
            sb.append("'>\n")
        }
        sb.append(value)
        writtenAtLeastOneField = true
    }

    override fun format(): String{
        context.fields.accept(this)
        sb.append("</table>\n")
        return sb.toString()
    }
}