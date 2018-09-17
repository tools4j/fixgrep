package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition
import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Ansi
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.MessageSpec
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.Stack;

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class HorizontalConsoleFieldFormatter(val fieldWriter: FieldWriter, formattingContext: FormattingContext, msgTextEffect: TextEffect): AbstractConsoleFieldFormatter(formattingContext , msgTextEffect) {
    val sb = StringBuilder()

    override fun finish() {
        fieldWriter.writeField(sb.toString())
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
}