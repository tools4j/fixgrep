package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields

class HighlightCriteriaTagValueEquals(val tag: Int, val match: String): HighlightCriteria {
    override fun matches(fix: Fields): HighlightCriteriaMatch{
        val field = fix.getField(tag)
        if(field == null){
            return HighlightCriteriaMatch.NO_MATCH
        }
        if(match.isEmpty()){
            if(!field.value.isEmpty()){
                return HighlightCriteriaMatch.NO_MATCH
            } else {
                return HighlightCriteriaMatch(field)
            }
        }
        if(field.value.valueRaw.equals(match)){
            return HighlightCriteriaMatch(field)
        } else {
            return HighlightCriteriaMatch.NO_MATCH
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightCriteriaTagValueEquals) return false

        if (tag != other.tag) return false
        if (match != other.match) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag
        result = 31 * result + match.hashCode()
        return result
    }

    override fun toString(): String {
        return "HighlightCriteriaTagValueEquals(tag=$tag, match='$match')"
    }


}