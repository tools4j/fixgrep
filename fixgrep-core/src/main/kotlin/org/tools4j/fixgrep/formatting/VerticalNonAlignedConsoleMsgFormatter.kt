package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fixgrep.highlights.HighlightedFields
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class VerticalNonAlignedConsoleMsgFormatter(val context: FormattingContext) : MsgFormatter(), FieldWriter {
    val sb = StringBuilder()
    var atLeastOneFieldWritten = false
    val pendingGroupRepeatNumber = AtomicBoolean(false)

    override fun getFieldVisitor(): FieldVisitor {
        return VerticalNonAlignedConsoleFieldFormatter(this, context, msgTextEffect, pendingGroupRepeatNumber)
    }

    override fun writeField(value: String) {
        if(atLeastOneFieldWritten){
            sb.append("\n")
        }
        atLeastOneFieldWritten = true
        sb.append(value)
    }

    override fun format(): String{
        context.fields.accept(this)
        sb.append("\n")
        return sb.toString()
    }
}