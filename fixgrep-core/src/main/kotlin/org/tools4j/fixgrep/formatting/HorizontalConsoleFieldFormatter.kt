package org.tools4j.fixgrep.formatting

import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class HorizontalConsoleFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext, msgTextEffect: TextEffect): AbstractConsoleFieldFormatter(formattingContext , msgTextEffect) {
    val sb = StringBuilder()

    override fun finish() {
        if(context.displayTag(tagRaw!!)) {
            fieldWriter.writeField(sb.toString())
        }
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
}