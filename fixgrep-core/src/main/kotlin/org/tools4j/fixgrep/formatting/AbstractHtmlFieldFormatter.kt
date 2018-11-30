package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
abstract class AbstractHtmlFieldFormatter(val formattingContext: FormattingContext): FieldFormatter(formattingContext) {
    val tagAppender: HtmlPartAppender by lazy {HtmlPartAppender(formattingContext.annotationPositions.tagAnnotationPosition, tagRaw, tagAnnotation, context.boldTagAndValue, "tag")}
    val valueAppender: HtmlPartAppender by lazy {HtmlPartAppender(formattingContext.annotationPositions.valueAnnotationPosition, valueRaw, valueAnnotation, context.boldTagAndValue, "value")}

    open fun appendEquals(sb: StringBuilder) {
        sb.append("<span class='equals");
        val boldTagAndValue = context.annotationPositions == AnnotationPositions.OUTSIDE_ANNOTATED && context.boldTagAndValue
        if(boldTagAndValue){
            sb.append(" bold")
        }
        sb.append("'>=</span>")
    }

    class HtmlPartAppender(annotationPosition: AnnotationPosition,
                           val raw: Any?,
                           val annotation: String?,
                           val boldTagAndValue: Boolean,
                           val cssClass: String): AbstractPartAppender(annotationPosition) {

        override fun appendRaw(sb: StringBuilder) {
            sb.append("<span class='$cssClass raw")
            if (boldTagAndValue && annotationPosition != AnnotationPosition.REPLACE) sb.append(" bold")
            sb.append("'>").append(raw).append("</span>")
        }

        override fun appendAnnotation(sb: StringBuilder) {
            sb.append("<span class='$cssClass annotation")
            sb.append("'>")
            if(annotationPosition != AnnotationPosition.REPLACE) sb.append("[")
            sb.append(annotation)
            if(annotationPosition != AnnotationPosition.REPLACE) sb.append("]")
            sb.append("</span>")
        }

        override fun hasAnnotation() = annotation != null
    }
}