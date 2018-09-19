package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.MessageSpec
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.*

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalAlignedHtmlFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext): FieldFormatter(formattingContext) {
    override fun finish() {
        if(context.displayTag(tagRaw!!)) {
            val sb = StringBuilder()
            sb.append("<tr class='field")
            if (context.annotationPositions != AnnotationPositions.NO_ANNOTATION) sb.append(" annotatedField")
            if (fieldTextEffect != TextEffect.NONE) sb.append(" " + fieldTextEffect.htmlClass)
            sb.append("'>")
            appendTag(sb)
            appendEquals(sb)
            appendValue(sb)
            sb.append("</tr>\n")
            fieldWriter.writeField(sb.toString())
        }
    }

    private fun appendEquals(sb: StringBuilder) {
        sb.append("<td class='equals");
        if(context.boldTagAndValue) sb.append(" bold")
        sb.append("'>=</td>")
    }

    fun appendTag(sb: StringBuilder): String{
        if(context.annotationPositions.tagAnnotationPosition == AnnotationPosition.NONE){
            this.appendTagRaw(sb)
        } else if(context.annotationPositions.tagAnnotationPosition == AnnotationPosition.BEFORE){
            if(tagAnnotation != null) appendTagAnnotation(sb)
            this.appendTagRaw(sb)
        } else {
            this.appendTagRaw(sb)
            if(tagAnnotation != null) appendTagAnnotation(sb)
        }
        return sb.toString()
    }

    fun appendTagAnnotation(sb: StringBuilder) {
        sb.append("<td class='tag-annotation")
        sb.append("'>")
        sb.append(tagAnnotation)
        sb.append("</td>")
    }

    fun appendTagRaw(sb: StringBuilder) {
        sb.append("<td class='tag-raw")
        if (context.boldTagAndValue) sb.append(" bold")
        sb.append("'>").append(tagRaw).append("</td>")
    }

    private fun appendValue(sb: StringBuilder) {
        if (context.annotationPositions.valueAnnotationPosition == AnnotationPosition.NONE) {
            appendValueRaw(sb)
        } else if (context.annotationPositions.valueAnnotationPosition == AnnotationPosition.BEFORE) {
            appendValueAnnotation(sb)
            appendValueRaw(sb)
        } else {
            appendValueRaw(sb)
            appendValueAnnotation(sb)
        }
    }

    fun appendValueAnnotation(sb: StringBuilder) {
        if (valueAnnotation != null){
            sb.append("<td class='value-annotation'>").append(valueAnnotation).append("</td>")
        }
    }

    fun appendValueRaw(sb: StringBuilder) {
        sb.append("<td class='value-raw")
        if (context.boldTagAndValue) sb.append(" bold")
        sb.append("'")
        if (valueAnnotation == null) sb.append(" colspan='2'")
        sb.append(">").append(valueRaw).append("</td>")
    }
}