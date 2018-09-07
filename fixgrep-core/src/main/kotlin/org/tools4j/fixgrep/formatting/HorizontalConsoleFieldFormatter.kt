package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Ansi
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.MessageSpec
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.Stack;

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class HorizontalConsoleFieldFormatter(val fieldWriter: FieldWriter, val formattingContext: FormattingContext, val msgTextEffect: TextEffect): FieldFormatter(formattingContext) {
    var independentlyMarkupTagsAndValuesAsBold: Boolean = true

    override fun finish() {
        val sb = StringBuilder()
        independentlyMarkupTagsAndValuesAsBold = context.boldTagAndValue && !msgTextEffect.contains(MiscTextEffect.Bold) && !fieldTextEffect.contains(MiscTextEffect.Bold)
        fieldTextEffect = msgTextEffect.compositeWith(fieldTextEffect)
        sb.append(fieldTextEffect.consoleTextBefore)
        appendTag(sb)
        appendEquals(sb)
        appendValue(sb)
        sb.append(fieldTextEffect.consoleTextAfter)
        fieldWriter.writeField(sb.toString())
    }

    private fun appendEquals(sb: StringBuilder) {
        val rawTagAndValueAreEitherSideOfEqualsAndAreBold = context.annotationPositions == AnnotationPositions.OUTSIDE_ANNOTATED && context.boldTagAndValue
        if (rawTagAndValueAreEitherSideOfEqualsAndAreBold && independentlyMarkupTagsAndValuesAsBold) sb.append(Ansi.Bold)
        sb.append("=")
        if (rawTagAndValueAreEitherSideOfEqualsAndAreBold && independentlyMarkupTagsAndValuesAsBold) sb.append(Ansi.Normal)
    }

    fun appendTag(sb: StringBuilder): String{
        if(context.annotationPositions.tagAnnotationPosition == AnnotationPosition.NONE){
            appendTagRaw(sb)
        } else if(context.annotationPositions.tagAnnotationPosition == AnnotationPosition.BEFORE){
            appendTagAnnotation(sb)
            appendTagRaw(sb)
        } else {
            appendTagRaw(sb)
            appendTagAnnotation(sb)
        }
        return sb.toString()
    }

    fun appendTagRaw(sb: StringBuilder) {
        if (independentlyMarkupTagsAndValuesAsBold) sb.append(Ansi.Bold)
        sb.append(tagRaw)
        if (independentlyMarkupTagsAndValuesAsBold) sb.append(Ansi.Normal)
    }

    fun appendTagAnnotation(sb: StringBuilder) {
        if(tagAnnotation != null) sb.append("[").append(tagAnnotation).append("]")
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

    fun appendValueRaw(sb: StringBuilder) {
        if (independentlyMarkupTagsAndValuesAsBold){
            sb.append(Ansi.Bold)
        }
        sb.append(valueRaw)
        if (independentlyMarkupTagsAndValuesAsBold){
            sb.append(Ansi.Normal)
        }
    }

    fun appendValueAnnotation(sb: StringBuilder) {
        if(valueAnnotation != null) sb.append("[").append(valueAnnotation).append("]")
    }
}