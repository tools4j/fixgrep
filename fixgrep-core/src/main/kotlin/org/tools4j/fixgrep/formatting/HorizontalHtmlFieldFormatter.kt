package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.AnnotationSpec
import org.tools4j.fix.Ansi
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class HorizontalHtmlFieldFormatter(val fieldWriter: FieldWriter, val annotationSpec: AnnotationSpec): FieldFormatter() {
    override fun finish() {
        val sb = StringBuilder()
        sb.append("<span class='field")
        if(annotationSpec.annotationPositions != AnnotationPositions.NO_ANNOTATION) sb.append(" annotatedField")
        if(fieldTextEffect != TextEffect.NONE) sb.append(" " + fieldTextEffect.htmlClass)
        sb.append("'>")
        appendTag(sb)
        appendEquals(sb)
        appendValue(sb)
        sb.append("</span>")
        fieldWriter.writeField(sb.toString())
    }

    private fun appendEquals(sb: StringBuilder) {
        sb.append("<span class='equals");
        if(annotationSpec.boldTagAndValue) sb.append(" bold")
        sb.append("'>=</span>")
    }

    fun appendTag(sb: StringBuilder): String{
        if(annotationSpec.annotationPositions.tagAnnotationPosition == AnnotationPosition.NONE){
            this.appendTagRaw(sb)
        } else if(annotationSpec.annotationPositions.tagAnnotationPosition == AnnotationPosition.BEFORE){
            if(tagAnnotation != null) appendTagAnnotation(sb)
            this.appendTagRaw(sb)
        } else {
            this.appendTagRaw(sb)
            if(tagAnnotation != null) appendTagAnnotation(sb)
        }
        return sb.toString()
    }

    fun appendTagAnnotation(sb: StringBuilder) {
        sb.append("<span class='tag annotation")
        sb.append("'>[")
        sb.append(tagAnnotation)
        sb.append("]</span>")
    }

    fun appendTagRaw(sb: StringBuilder) {
        sb.append("<span class='tag tagNumber")
        if (annotationSpec.boldTagAndValue) sb.append(" bold")
        sb.append("'>").append(tagRaw).append("</span>")
    }

    private fun appendValue(sb: StringBuilder) {
        if (annotationSpec.annotationPositions.valueAnnotationPosition == AnnotationPosition.NONE) {
            appendValueRaw(sb)
        } else if (annotationSpec.annotationPositions.valueAnnotationPosition == AnnotationPosition.BEFORE) {
            appendValueAnnotation(sb)
            appendValueRaw(sb)
        } else {
            appendValueRaw(sb)
            appendValueAnnotation(sb)
        }
    }

    fun appendValueAnnotation(sb: StringBuilder) {
        sb.append("[").append(valueAnnotation).append("]")
    }

    fun appendAnnotation(sb: StringBuilder) {
        sb.append("<span class='value annotation'>[").append(valueAnnotation).append("]</span>")
    }

    fun appendValueRaw(sb: StringBuilder) {
        sb.append("<span class='value rawValue")
        if (annotationSpec.boldTagAndValue) sb.append(" bold")
        sb.append("'>").append(valueRaw).append("</span>")
    }
}