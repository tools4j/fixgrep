package org.tools4j.fixgrep.highlights

import org.tools4j.fixgrep.texteffect.TextEffectParser

/**
 * User: ben
 * Date: 5/04/2018
 * Time: 6:59 AM
 */
class HighlightParser(_defaultHighlightTextEffects: DefaultHighlightTextEffects) {
    constructor() : this(DefaultHighlightTextEffects.DEFAULT)

    val defaultHighlightTextEffects: DefaultHighlightTextEffects
    init{
        defaultHighlightTextEffects = DefaultHighlightTextEffects(_defaultHighlightTextEffects)
    }

    fun parse(expression: String): Highlight {
        val expressions = expression.split(',')
        return parse(expressions)
    }

    fun parse(expressions: List<String>): Highlight{
        defaultHighlightTextEffects.reset()
        val highlights = ArrayList<Highlight>()
        expressions.forEach {
            highlights.add(parseExpression(it))
        }
        return Highlights(highlights)
    }

    private fun parseExpression(expression: String): Highlight {
        if(expression.isEmpty()){
            return Highlight.NO_HIGHLIGHT
        }

        val parts = expression.split(":")
        val mutableParts: MutableList<String> = ArrayList(parts)

        //1. The first 'part', the 'criteria' is mandatory
        val criteriaStr = mutableParts.removeAt(0)
        val criteria = HighlightCriteriaParser().parse(criteriaStr)

        //2. If the 'scope' is specified, it _must_ be the last item in the.  Check, and extract if there
        val scope = if(!mutableParts.isEmpty() && HighlightScope.containsIgnoringCase(mutableParts.last())){
                HighlightScope.valueOf(mutableParts.removeAt(mutableParts.lastIndex))
            } else {
                //Otherwise default value
                HighlightScope.Field
            }

        //3. The 'TextEffect' is the last to extract.  It sits in the middle.  It can be specified in 0-to-many parts.
        //  If specified with zero parts, i.e. it's not there, we fetch the next default Highlight as the foreground color
        //  If one-to-many parts, we let the TextEffectParser handle it
        val textEffect = if(mutableParts.isEmpty()){
            defaultHighlightTextEffects.next()
        } else {
            TextEffectParser().parse(mutableParts.joinToString(":"))
        }

        return HighlightImpl(criteria, scope, textEffect)
    }
}