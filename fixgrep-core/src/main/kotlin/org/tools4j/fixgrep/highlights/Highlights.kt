package org.tools4j.fixgrep.highlights;

import org.tools4j.fix.Delimiter
import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 5:55 PM
 */
class Highlights(val highlights: List<Highlight>): Highlight{
    override fun applyToDelimiter(fields: Fields, delimiter: Delimiter): Delimiter {
        var outputDelimiter = delimiter
        highlights.forEach {
            outputDelimiter = it.applyToDelimiter(fields, outputDelimiter)
        }
        return outputDelimiter
    }

    override fun applyToFields(fields: Fields): Fields {
        var outputFields = fields
        highlights.forEach {
            outputFields = it.applyToFields(outputFields)
        }
        return outputFields
    }
}
