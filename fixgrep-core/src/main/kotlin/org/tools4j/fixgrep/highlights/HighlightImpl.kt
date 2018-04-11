package org.tools4j.fixgrep.highlights;

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 5:56 PM
 */
class HighlightImpl(val criteria: HighlightCriteria, val action: HighlightAction): Highlight {
    override fun apply(fields: Fields): Fields {
        val match = criteria.matches(fields)
        return if(match.matches){
            action.apply(fields, match.matchingFields)
        } else {
            fields
        }
    }
}
