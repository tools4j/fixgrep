package org.tools4j.fixgrep.formatting

import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.concurrent.atomic.AtomicBoolean

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalNonAlignedConsoleFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext, msgTextEffect: TextEffect, val pendingGroupRepeatNumber: AtomicBoolean): AbstractConsoleFieldFormatter(formattingContext , msgTextEffect) {
    companion object {
        val INDENT_CHAR_COUNT = 8
    }
    val sb = StringBuilder()

    override fun finish() {
        if(context.displayTag(tagRaw!!)) {
            fieldWriter.writeField(sb.toString())
        }
    }

    val indent: String by lazy {
        " ".repeat(INDENT_CHAR_COUNT)
    }

    override fun onFieldBody() {
        if(context.displayTag(tagRaw!!)) {
            boldTagAndValue = context.boldTagAndValue && !msgTextEffect.contains(MiscTextEffect.Bold) && !fieldTextEffect.contains(MiscTextEffect.Bold)
            fieldTextEffect = msgTextEffect.compositeWith(fieldTextEffect)
            sb.append(fieldTextEffect.consoleTextBefore)
            tagAppender.append(sb)
            appendEquals(sb)
            valueAppender.append(sb)
            sb.append(fieldTextEffect.consoleTextAfter)
        }
    }

    override fun onGroupEnter() {
        if(!context.indentGroupRepeats) return
        if(!context.displayTag(tagRaw!!)) return
        doFirst {
            sb.append(indent.repeat(context.groupStack.size() - 1))
        }
    }

    override fun onGroupRepeatEnter() {
        if(!context.indentGroupRepeats) return
        doFirst {
            onGroupRepeatEnterAction()
        }
    }

    private fun onGroupRepeatEnterAction() {
        if (!context.displayTag(tagRaw!!)) {
            pendingGroupRepeatNumber.set(true)
        } else {
            pendingGroupRepeatNumber.set(false)
            sb.append(indent.repeat(context.groupStack.size() - 1))
            sb.append(" ".repeat(INDENT_CHAR_COUNT / 2)).append("${context.groupStack.getCurrentRepeatNumber()}.".padEnd(INDENT_CHAR_COUNT / 2, ' '))
        }
    }

    override fun onGroupRepeatExit() {
        pendingGroupRepeatNumber.set(false)
    }

    override fun onGroupExit() {
        pendingGroupRepeatNumber.set(false)
    }

    override fun onSubsequentFieldInGroupRepeat() {
        if(!context.indentGroupRepeats) return
        if(!context.displayTag(tagRaw!!)) return
        doFirst{
            if(pendingGroupRepeatNumber.get()) onGroupRepeatEnterAction()
            else sb.append(indent.repeat(context.groupStack.size()))
        }
    }
}