package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field
import org.tools4j.fix.FieldVisitor
import org.tools4j.fixgrep.texteffect.CompositeTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

class HighlightedField(val field: Field, textEffectParam: TextEffect): Field by field {
    val textEffect: TextEffect by lazy {
        if(field is HighlightedField){
            CompositeTextEffect(linkedSetOf(field.textEffect, textEffectParam))
        } else {
            textEffectParam
        }
    }

    override fun accept(fieldVisitor: FieldVisitor) {
        fieldVisitor.visit(this)
    }
}