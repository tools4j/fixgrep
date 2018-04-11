package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields

class HighlightCriteriaLogicalAnd(val criteria: List<HighlightCriteria>): HighlightCriteria {
    override fun matches(fix: Fields): HighlightCriteriaMatch {
        var returnCriteria = HighlightCriteriaMatch.EMPTY_MATCH
        criteria.forEach {
            returnCriteria = returnCriteria.and(it.matches(fix))
        }
        return returnCriteria
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightCriteriaLogicalAnd) return false

        if (criteria != other.criteria) return false

        return true
    }

    override fun hashCode(): Int {
        return criteria.hashCode()
    }

    override fun toString(): String {
        return "HighlightCriteriaLogicalAnd(criteria=$criteria)"
    }
}