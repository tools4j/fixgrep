package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fixgrep.highlights.HighlightedFields
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.*

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class VerticalNonAlignedConsoleMsgFormatter(val context: FormattingContext) : MsgFormatter(), FieldWriter {
    val sb = StringBuilder()
    var writtenAtLeastOneField = false

    override fun getFieldVisitor(): FieldVisitor {
        return VerticalNonAlignedHtmlFieldFormatter(this, context)
    }

    override fun writeField(value: String) {
        if(!writtenAtLeastOneField){
            sb.append("<div class='fields")
            if(msgTextEffect != TextEffect.NONE) sb.append(" ${msgTextEffect.htmlClass}")
            sb.append("'>\n")
        }
        sb.append(value)
        writtenAtLeastOneField = true
    }

    override fun format(): String{
        context.fields.accept(this)
        sb.append("</div>\n")
        return sb.toString()
    }
}