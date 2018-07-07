package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Delimiter
import org.tools4j.fix.Field
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.TextEffect
import java.util.stream.Collectors

class HighlightAction(val criteria: HighlightCriteria, val scope: HighlightScope, val textEffect: TextEffect): Highlight {
    override fun apply(fields: Fields): Fields {
        val matchingTags = criteria.matches(fields)
        return FieldsImpl(fields.stream().map {
            if (scope == HighlightScope.Line || matchingTags.matchingFields.contains(it)) {
                HighlightedField(it, textEffect)
            } else {
                it
            }
        }.collect(Collectors.toList()))
    }

    override fun apply(delimiter: Delimiter): Delimiter {
        return if(scope == HighlightScope.Line) HighlightedDelimiter(delimiter.delimiter, textEffect) else delimiter
    }
}