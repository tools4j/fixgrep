package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalNonAlignedHtmlFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext): FieldFormatter(formattingContext) {
    val sb = StringBuilder()

    override fun onFieldBody() {
        if(context.displayTag(super.tagRaw!!)) {
            sb.append("<div class='field")
            if (context.annotationPositions != AnnotationPositions.NO_ANNOTATION) sb.append(" annotatedField")
            if (fieldTextEffect != TextEffect.NONE) sb.append(" " + fieldTextEffect.htmlClass)
            sb.append("'>")
            appendTag(sb)
            appendEquals(sb)
            appendValue(sb)
            sb.append("</div>\n")
        }
    }

    override fun finish() {
        fieldWriter.writeField(sb.toString())
    }

    override fun onGroupEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("<div class='group'>\n")
        }
        doLast {
            sb.append("<div class='group-repeats'>\n")
        }
    }

    override fun onGroupRepeatEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("<div class='group-repeat'>\n")
            sb.append("<div class='group-repeat-number'>${context.groupStack.getCurrentRepeatNumber()}.</div>\n")
        }
    }

    override fun onGroupRepeatExit() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("</div><!--group repeat exit-->\n")
        }
    }

    override fun onGroupExit() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("</div><!--group repeats exit-->\n")
            sb.append("</div><!--group exit-->\n")
        }
    }

    private fun appendEquals(sb: StringBuilder) {
        sb.append("<span class='equals");
        if(context.boldTagAndValue) sb.append(" bold")
        sb.append("'>=</span>")
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
        sb.append("<span class='tag-annotation")
        sb.append("'>[")
        sb.append(tagAnnotation)
        sb.append("]</span>")
    }

    fun appendTagRaw(sb: StringBuilder) {
        sb.append("<span class='tag-raw")
        if (context.boldTagAndValue) sb.append(" bold")
        sb.append("'>").append(tagRaw).append("</span>")
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
            sb.append("<span class='value-annotation'>[").append(valueAnnotation).append("]</span>")
        }
    }

    fun appendValueRaw(sb: StringBuilder) {
        sb.append("<span class='value-raw")
        if (context.boldTagAndValue) sb.append(" bold")
        sb.append("'")
        sb.append(">").append(valueRaw).append("</span>")
    }

    companion object {
        val INDENT_CHAR_COUNT = 4
    }
}