package org.tools4j.fixgrep.highlights;

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 5:55 PM
 */
class Highlights(val highlights: List<Highlight>): Highlight{
    override fun apply(fields: Fields): Fields {
        var outputFields = fields
        highlights.forEach {
            outputFields = it.apply(outputFields)
        }
        return outputFields
    }
}
