package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:24 PM
 */
class HighlightCriteriaAlwaysTrue: HighlightCriteria {
    override fun matches(fix: Fields): HighlightCriteriaMatch {
        return HighlightCriteriaMatch.EMPTY_MATCH
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightCriteriaAlwaysTrue) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}