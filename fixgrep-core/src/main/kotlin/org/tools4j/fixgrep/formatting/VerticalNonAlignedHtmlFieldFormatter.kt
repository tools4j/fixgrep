package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.concurrent.atomic.AtomicInteger

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalNonAlignedHtmlFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext, var uid: AtomicInteger): AbstractHtmlFieldFormatter(formattingContext) {
    val sb = StringBuilder()

    override fun onFieldBody() {
        if(context.displayTag(super.tagRaw!!)) {
            sb.append("<div class='field")
            if (context.annotationPositions != AnnotationPositions.NO_ANNOTATION) sb.append(" annotatedField")
            if (fieldTextEffect != TextEffect.NONE) sb.append(" " + fieldTextEffect.htmlClass)
            sb.append("'><!--uid:${getUniqueId()}-->")
            tagAppender.append(sb)
            appendEquals(sb)
            valueAppender.append(sb)
            sb.append("</div><!--uid:${getUniqueId()}-->\n")
        }
    }

    override fun finish() {
        fieldWriter.writeField(sb.toString())
    }

    override fun onGroupEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("<div class='group'><!--uid:${getUniqueId()}-->\n")
        }
        doLast {
            sb.append("<div class='group-repeats'><!--uid:${getUniqueId()}-->\n")
        }
    }

    override fun onGroupRepeatEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("<div class='group-repeat'><!--uid:${getUniqueId()}-->\n")
            sb.append("<div class='group-repeat-number'><!--uid:${getUniqueId()}-->${context.groupStack.getCurrentRepeatNumber()}.</div>\n")
        }
    }

    override fun onGroupRepeatExit() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("</div><!--group repeat exit uid:${getUniqueId()}-->\n")
        }
    }

    override fun onGroupExit() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append("</div><!--group repeats exit uid::${getUniqueId()}-->\n")
            sb.append("</div><!--group exit uid:${getUniqueId()}-->\n")
        }
    }

    private fun getUniqueId(): Int {
        val uid = uid.getAndIncrement()
        return uid
    }
}