package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields

class HighlightCriteriaTagIsPresent(val tag: Int): HighlightCriteria {
    override fun matches(fix: Fields): HighlightCriteriaMatch{
        return HighlightCriteriaMatch(fix.exists(tag), listOfNotNull(fix.getField(tag)))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightCriteriaTagIsPresent) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag
    }

    override fun toString(): String {
        return "HighlightCriteriaTagIsPresent(tag=$tag)"
    }
}