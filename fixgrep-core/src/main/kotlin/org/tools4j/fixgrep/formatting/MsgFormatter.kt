package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsVisitor
import org.tools4j.fixgrep.highlights.HighlightedFields
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/11/2018
 * Time: 6:07 PM
 */
abstract class MsgFormatter(): FieldsVisitor {
    var msgTextEffect: TextEffect = TextEffect.NONE

    abstract fun format(): String;

    override fun visit(fields: Fields) {
        if(fields is HighlightedFields){
            msgTextEffect = fields.textEffect
        }
    }
}