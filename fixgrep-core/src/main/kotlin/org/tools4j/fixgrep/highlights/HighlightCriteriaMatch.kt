package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field

/**
 * User: ben
 * Date: 5/04/2018
 * Time: 5:24 PM
 */
class HighlightCriteriaMatch(val matches: Boolean, val matchingFields: List<Field>){
    constructor(matchingField: Field) : this(true, listOf(matchingField))
    constructor(matchingFields: List<Field>) : this(true, matchingFields)

    fun and(other: HighlightCriteriaMatch): HighlightCriteriaMatch{
        return HighlightCriteriaMatch(this.matches && other.matches, this.matchingFields + other.matchingFields)
    }

    override fun toString(): String {
        return "HighlightCriteriaMatch(matches=$matches, matchingFields=$matchingFields)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightCriteriaMatch) return false

        if (matches != other.matches) return false
        if (matchingFields != other.matchingFields) return false

        return true
    }

    override fun hashCode(): Int {
        var result = matches.hashCode()
        result = 31 * result + matchingFields.hashCode()
        return result
    }

    companion object {
        val NO_MATCH = HighlightCriteriaMatch(false, emptyList())
        val EMPTY_MATCH = HighlightCriteriaMatch(true, emptyList())
    }


}