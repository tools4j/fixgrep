package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class HorizontalHtmlFieldFormatter(val fieldWriter: FieldWriter, context: FormattingContext): AbstractHtmlFieldFormatter(context) {
    override fun finish() {
        if(context.displayTag(tagRaw!!)) {
            val sb = StringBuilder()
            sb.append("<span class='field")
            if (context.annotationPositions != AnnotationPositions.NO_ANNOTATION) sb.append(" annotatedField")
            if (fieldTextEffect != TextEffect.NONE) sb.append(" " + fieldTextEffect.htmlClass)
            sb.append("'>")
            tagAppender.append(sb)
            appendEquals(sb)
            valueAppender.append(sb)
            sb.append("</span>")
            fieldWriter.writeField(sb.toString())
        }
    }
}