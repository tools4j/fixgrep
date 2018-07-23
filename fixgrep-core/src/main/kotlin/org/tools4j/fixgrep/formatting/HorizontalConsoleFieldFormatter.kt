package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.AnnotationSpec
import org.tools4j.fix.Ansi
import org.tools4j.fixgrep.texteffect.MiscTextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class HorizontalConsoleFieldFormatter(val fieldWriter: FieldWriter, val annotationSpec: AnnotationSpec): FieldFormatter() {

    override fun finish() {
        val sb = StringBuilder()
        sb.append(fieldTextEffect.consoleTextBefore)
        appendTag(sb)
        appendEquals(sb)
        appendValue(sb)
        sb.append(fieldTextEffect.consoleTextAfter)
        fieldWriter.writeField(sb.toString())
    }

    private fun appendEquals(sb: StringBuilder) {
        val rawTagAndValueAreEitherSideOfEqualsAndAreBold = annotationSpec.annotationPositions == AnnotationPositions.OUTSIDE_ANNOTATED && annotationSpec.boldTagAndValue
        if (rawTagAndValueAreEitherSideOfEqualsAndAreBold && !fieldTextEffect.contains(MiscTextEffect.Bold)) sb.append(Ansi.Bold)
        sb.append("=")
        if (rawTagAndValueAreEitherSideOfEqualsAndAreBold && !fieldTextEffect.contains(MiscTextEffect.Bold)) sb.append(Ansi.Normal)
    }

    fun appendTag(sb: StringBuilder): String{
        if(annotationSpec.annotationPositions.tagAnnotationPosition == AnnotationPosition.NONE){
            appendTagRaw(sb)
        } else if(annotationSpec.annotationPositions.tagAnnotationPosition == AnnotationPosition.BEFORE){
            appendTagAnnotation(sb)
            appendTagRaw(sb)
        } else {
            appendTagRaw(sb)
            appendTagAnnotation(sb)
        }
        return sb.toString()
    }

    fun appendTagRaw(sb: StringBuilder) {
        if (annotationSpec.boldTagAndValue && !fieldTextEffect.contains(MiscTextEffect.Bold)) sb.append(Ansi.Bold)
        sb.append(tagRaw)
        if (annotationSpec.boldTagAndValue && !fieldTextEffect.contains(MiscTextEffect.Bold)) sb.append(Ansi.Normal)
    }

    fun appendTagAnnotation(sb: StringBuilder) {
        if(tagAnnotation != null) sb.append("[").append(tagAnnotation).append("]")
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

    fun appendValueRaw(sb: StringBuilder) {
        if (annotationSpec.boldTagAndValue && !fieldTextEffect.contains(MiscTextEffect.Bold)) sb.append(Ansi.Bold)
        sb.append(valueRaw)
        if (annotationSpec.boldTagAndValue && !fieldTextEffect.contains(MiscTextEffect.Bold)) sb.append(Ansi.Normal)
    }

    fun appendValueAnnotation(sb: StringBuilder) {
        if(valueAnnotation != null) sb.append("[").append(valueAnnotation).append("]")
    }
}