package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.stream.Collectors

class HighlightImpl(val criteria: HighlightCriteria, val scope: HighlightScope, val textEffect: TextEffect): Highlight {
    override fun apply(fields: Fields): Fields {
        val matchingTags = criteria.matches(fields)
        if(matchingTags.matchingFields.isEmpty()) return fields

        val individuallyHighlightedFields = FieldsImpl(fields.stream().map {
            if (scope == HighlightScope.Field && matchingTags.matchingFields.contains(it)) {
                HighlightedField(it, textEffect)
            } else {
                it
            }
        }.collect(Collectors.toList()))

        val previousMsgTextEffect = if(fields is HighlightedFields) fields.textEffect else TextEffect.NONE
        val msgTextEffect = if(scope == HighlightScope.Line && !matchingTags.matchingFields.isEmpty()){
            previousMsgTextEffect.compositeWith(textEffect)
        } else {
            previousMsgTextEffect
        }
        return HighlightedFields(individuallyHighlightedFields, msgTextEffect)
    }
}