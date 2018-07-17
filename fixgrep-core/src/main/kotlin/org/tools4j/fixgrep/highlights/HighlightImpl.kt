package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Delimiter
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.stream.Collectors

class HighlightImpl(val criteria: HighlightCriteria, val scope: HighlightScope, val textEffect: TextEffect): Highlight {
    override fun apply(fields: Fields): Fields {
        val matchingTags = criteria.matches(fields)
        if(matchingTags.matchingFields.isEmpty()) return fields

        if(scope == HighlightScope.Line){
            return HighlightedFields(fields, textEffect)
        } else {
            return FieldsImpl(fields.stream().map {
                if (matchingTags.matchingFields.contains(it)) {
                    HighlightedField(it, textEffect)
                } else {
                    it
                }
            }.collect(Collectors.toList()))
        }
    }
}