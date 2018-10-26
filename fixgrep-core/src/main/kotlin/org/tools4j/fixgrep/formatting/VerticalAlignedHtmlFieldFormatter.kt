package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalAlignedHtmlFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext): FieldFormatter(formattingContext) {
    val tagAppender: VerticalAlignedHtmlPartAppender by lazy { VerticalAlignedHtmlPartAppender(formattingContext.annotationPositions.tagAnnotationPosition, tagRaw, tagAnnotation, context.boldTagAndValue, "tag") }
    val valueAppender: VerticalAlignedHtmlPartAppender by lazy { VerticalAlignedHtmlPartAppender(formattingContext.annotationPositions.valueAnnotationPosition, valueRaw, valueAnnotation, context.boldTagAndValue, "value") }

    override fun finish() {
        if(context.displayTag(tagRaw!!)) {
            val sb = StringBuilder()
            sb.append("<tr class='field")
            if (context.annotationPositions != AnnotationPositions.NO_ANNOTATION) sb.append(" annotatedField")
            if (fieldTextEffect != TextEffect.NONE) sb.append(" " + fieldTextEffect.htmlClass)
            sb.append("'>")
            tagAppender.append(sb)
            appendEquals(sb)
            valueAppender.append(sb)
            sb.append("</tr>\n")
            fieldWriter.writeField(sb.toString())
        }
    }

    private fun appendEquals(sb: StringBuilder) {
        sb.append("<td class='equals");
        val boldTagAndValue = context.annotationPositions == AnnotationPositions.OUTSIDE_ANNOTATED && context.boldTagAndValue
        if(boldTagAndValue) sb.append(" bold")
        sb.append("'>=</td>")
    }

    class VerticalAlignedHtmlPartAppender(annotationPosition: AnnotationPosition,
                                          val raw: Any?,
                                          val annotation: String?,
                                          val boldTagAndValue: Boolean,
                                          val cssClass: String): AbstractPartAppender(annotationPosition) {

        override fun appendRaw(sb: StringBuilder) {
            sb.append("<td class='$cssClass raw")
            if (boldTagAndValue && annotationPosition != AnnotationPosition.REPLACE) sb.append(" bold")
            sb.append("'")
            if (annotation == null || annotationPosition == AnnotationPosition.NONE) sb.append(" colspan='2'")
            sb.append(">").append(raw).append("</td>")
        }

        override fun appendAnnotation(sb: StringBuilder) {
            if (annotation != null){
                sb.append("<td class='$cssClass annotation'")
                if (annotationPosition == AnnotationPosition.REPLACE) sb.append(" colspan='2'")
                sb.append(">").append(annotation).append("</td>")
            }
        }

        override fun hasAnnotation() = annotation != null
    }
}