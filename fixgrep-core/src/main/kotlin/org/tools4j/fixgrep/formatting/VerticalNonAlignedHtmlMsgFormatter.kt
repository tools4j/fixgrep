package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*
import org.tools4j.fixgrep.highlights.HighlightedFields
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class VerticalNonAlignedHtmlMsgFormatter(val fields: Fields, val annotationPositions: AnnotationPositions, val boldTagAndValues: Boolean) : FieldWriter, MsgFormatter, FieldsVisitor {
    var msgTextEffect: TextEffect = TextEffect.NONE
    val sb = StringBuilder()
    var writtenAtLeastOneField = false

    override fun getFieldVisitor(): FieldVisitor {
        return VerticalNonAlignedHtmlFieldFormatter(this, annotationPositions, boldTagAndValues)
    }

    override fun visit(fields: Fields) {
        if(fields is HighlightedFields){
            msgTextEffect = fields.textEffect
        }
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
        fields.accept(this)
        sb.append("</div>\n")
        return sb.toString()
    }
}