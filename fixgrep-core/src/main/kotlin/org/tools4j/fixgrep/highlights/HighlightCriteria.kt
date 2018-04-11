package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields

interface HighlightCriteria {
    fun matches(fix: Fields): HighlightCriteriaMatch
}