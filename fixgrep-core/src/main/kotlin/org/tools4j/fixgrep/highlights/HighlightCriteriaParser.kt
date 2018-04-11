package org.tools4j.fixgrep.highlights

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:04 PM
 */
class HighlightCriteriaParser {
    fun parse(expression: String): HighlightCriteria {
        if(expression.isEmpty()) return HighlightCriteriaAlwaysTrue()
        if(expression.contains("&&")) {
            val criteriaList = ArrayList<HighlightCriteria>()
            expression.split("&&").forEach {
                criteriaList.add(parseCriteria(it))
            }
            return HighlightCriteriaLogicalAnd(criteriaList)
        } else {
            return parseCriteria(expression)
        }
    }

    private fun parseCriteria(singleCriteriaExpression: String): HighlightCriteria {
        if(singleCriteriaExpression.contains('=')){
            val leftAndRightOfEquals = singleCriteriaExpression.split('=')
            return HighlightCriteriaTagValueMatches(leftAndRightOfEquals[0].toInt(), leftAndRightOfEquals[1])
        } else {
            return HighlightCriteriaTagIsPresent(singleCriteriaExpression.toInt())
        }
    }
}