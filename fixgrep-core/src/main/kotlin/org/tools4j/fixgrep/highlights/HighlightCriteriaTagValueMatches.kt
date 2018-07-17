package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import java.util.regex.Pattern

class HighlightCriteriaTagValueMatches(val tag: Int, val matchStr: String): HighlightCriteria {
    val matchPattern: Pattern by lazy {
        Pattern.compile(matchStr)
    }

    override fun matches(fix: Fields): HighlightCriteriaMatch{
        val field = fix.getField(tag)
        if(field == null){
            return HighlightCriteriaMatch.NO_MATCH
        }
        if(matchStr.isEmpty()){
            if(!field.value.isEmpty()){
                return HighlightCriteriaMatch.NO_MATCH
            } else {
                return HighlightCriteriaMatch(field)
            }
        }
        val matcher = matchPattern.matcher(field.value.valueRaw)
        if(matcher.find()){
            return HighlightCriteriaMatch(field)
        } else {
            return HighlightCriteriaMatch.NO_MATCH
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightCriteriaTagValueMatches) return false

        if (tag != other.tag) return false
        if (matchStr != other.matchStr) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag
        result = 31 * result + matchStr.hashCode()
        return result
    }

    override fun toString(): String {
        return "HighlightCriteriaTagValueMatches(tag=$tag, match='$matchStr')"
    }


}