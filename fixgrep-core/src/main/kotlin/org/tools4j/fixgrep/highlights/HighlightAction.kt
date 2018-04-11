package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.stream.Collectors

class HighlightAction(val scope: HighlightScope, val textEffect: TextEffect) {
    fun apply(fields: Fields, matchingTags: List<Field>): Fields {
        return FieldsImpl(fields.stream().map {
            if (scope == HighlightScope.Line || matchingTags.contains(it)) {
                HighlightedField(it, textEffect)
            } else {
                it
            }
        }.collect(Collectors.toList()))
    }
}