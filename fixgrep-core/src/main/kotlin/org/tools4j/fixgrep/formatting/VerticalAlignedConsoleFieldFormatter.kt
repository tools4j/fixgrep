package org.tools4j.fixgrep.formatting

import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:39 AM
 */
class VerticalAlignedConsoleFieldFormatter(val msgFormatter: VerticalAlignedConsoleMsgFormatter, formattingContext: FormattingContext, msgTextEffect: TextEffect): AbstractConsoleFieldFormatter(formattingContext , msgTextEffect) {
    var fieldDetails: VerticalAlignedConsoleMsgFormatter.FieldDetails? = null

    override fun finish() {
        if(context.displayTag(tagRaw!!)) {
            msgFormatter.writeFieldDetails(fieldDetails!!)
        }
    }

    override fun onFieldBody() {
        if(context.displayTag(tagRaw!!)) {
            //Run first without any bold effects, so that we can gather the 'true' widths of the fields, to get the max widths for alignment
            val tagWithoutTextEffectsSb = StringBuilder()
            tagAppender.append(tagWithoutTextEffectsSb)

            val equalsWithoutTextEffectsSb = StringBuilder()
            appendEquals(equalsWithoutTextEffectsSb)

            val valueWithoutTextEffectsSb = StringBuilder()
            valueAppender.append(valueWithoutTextEffectsSb)

            //Now the 'normal' run, setting bold property if required
            boldTagAndValue = context.boldTagAndValue && !msgTextEffect.contains(MiscTextEffect.Bold) && !fieldTextEffect.contains(MiscTextEffect.Bold)
            fieldTextEffect = msgTextEffect.compositeWith(fieldTextEffect)

            //Second run WITH bold effects (if configured that way)
            fieldTextEffect.consoleTextBefore
            val tagWithTextEffectsSb = StringBuilder()
            tagAppender.append(tagWithTextEffectsSb)

            val equalsWithTextEffectsSb = StringBuilder()
            appendEquals(equalsWithTextEffectsSb)

            val valueWithTextEffectsSb = StringBuilder()
            valueAppender.append(valueWithTextEffectsSb)

            fieldTextEffect.consoleTextAfter

            fieldDetails = VerticalAlignedConsoleMsgFormatter.FieldDetails(
                    tagWithoutTextEffectsSb.toString(),
                    equalsWithoutTextEffectsSb.toString(),
                    valueWithoutTextEffectsSb.toString(),
                    tagWithTextEffectsSb.toString(),
                    equalsWithTextEffectsSb.toString(),
                    valueWithTextEffectsSb.toString(),
                    fieldTextEffect.consoleTextBefore,
                    fieldTextEffect.consoleTextAfter
            )
        }
    }
}