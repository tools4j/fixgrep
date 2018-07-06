package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Fields
import org.tools4j.fixgrep.highlights.HighlightAction
import org.tools4j.fixgrep.highlights.HighlightCriteria

/**
 * User: ben
 * Date: 7/6/2018
 * Time: 6:39 AM
 */
interface FormattedFields: Fields {
    fun highlight(criteria: HighlightCriteria, action: HighlightAction): FormattedFields
    fun toConsoleText(): String
    fun toHtml(): String
}