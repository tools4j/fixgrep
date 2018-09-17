package org.tools4j.fixgrep.formatting

import com.google.common.base.Strings.padEnd
import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalNonAlignedConsoleFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext, msgTextEffect: TextEffect): AbstractConsoleFieldFormatter(formattingContext , msgTextEffect) {
    companion object {
        val INDENT_CHAR_COUNT = 8
    }
    val sb = StringBuilder()

    override fun finish() {
        fieldWriter.writeField(sb.toString())
    }

    val indent: String by lazy {
        " ".repeat(INDENT_CHAR_COUNT)
    }

    override fun onFieldBody() {
        independentlyMarkupTagsAndValuesAsBold = context.boldTagAndValue && !msgTextEffect.contains(MiscTextEffect.Bold) && !fieldTextEffect.contains(MiscTextEffect.Bold)
        fieldTextEffect = msgTextEffect.compositeWith(fieldTextEffect)
        sb.append(fieldTextEffect.consoleTextBefore)
        appendTag(sb)
        appendEquals(sb)
        appendValue(sb)
        sb.append(fieldTextEffect.consoleTextAfter)
    }

    override fun onGroupEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append(indent.repeat(context.groupStack.size() - 1))
        }
    }

    override fun onGroupRepeatEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append(indent.repeat(context.groupStack.size() - 1))
            sb.append(" ".repeat(INDENT_CHAR_COUNT/2)).append("${context.groupStack.getCurrentRepeatNumber()}.".padEnd(INDENT_CHAR_COUNT/2, ' '))
        }
    }

    override fun onGroupRepeatExit() {
        //noop
    }

    override fun onGroupExit() {
        //noop
    }

    override fun onSubsequentFieldInGroupRepeat() {
        if(!context.indentGroupRepeats) return
        doFirst {
            sb.append(indent.repeat(context.groupStack.size()))
        }
    }
}