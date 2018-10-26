package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Ansi
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
abstract class AbstractConsoleFieldFormatter(val formattingContext: FormattingContext, val msgTextEffect: TextEffect): FieldFormatter(formattingContext) {
    var boldTagAndValue: Boolean = true
    val tagAppender: ConsolePartAppender by lazy {ConsolePartAppender(formattingContext.annotationPositions.tagAnnotationPosition, tagRaw, tagAnnotation, boldTagAndValue)}
    val valueAppender: ConsolePartAppender by lazy {ConsolePartAppender(formattingContext.annotationPositions.valueAnnotationPosition, valueRaw, valueAnnotation, boldTagAndValue)}

    internal fun appendEquals(sb: StringBuilder) {
        val boldTagAndValue = context.annotationPositions == AnnotationPositions.OUTSIDE_ANNOTATED && context.boldTagAndValue
        if (boldTagAndValue && this.boldTagAndValue) sb.append(Ansi.Bold)
        sb.append("=")
        if (boldTagAndValue && this.boldTagAndValue) sb.append(Ansi.Normal)
    }

    class ConsolePartAppender(annotationPosition: AnnotationPosition,
                              val raw: Any?,
                              val annotation: String?,
                              val boldTagAndValue: Boolean): AbstractPartAppender(annotationPosition) {
        override fun appendRaw(sb: StringBuilder) {
            if (boldTagAndValue && annotationPosition != AnnotationPosition.REPLACE) sb.append(Ansi.Bold)
            sb.append(raw)
            if (boldTagAndValue && annotationPosition != AnnotationPosition.REPLACE) sb.append(Ansi.Normal)
        }

        override fun appendAnnotation(sb: StringBuilder) {
            if (annotationPosition != AnnotationPosition.REPLACE) sb.append("[")
            sb.append(annotation)
            if (annotationPosition != AnnotationPosition.REPLACE) sb.append("]")
        }

        override fun hasAnnotation() = annotation != null
    }
}