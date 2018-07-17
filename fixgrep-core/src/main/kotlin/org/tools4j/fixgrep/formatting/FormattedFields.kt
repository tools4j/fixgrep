package org.tools4j.fixgrep.formatting

import org.tools4j.fix.Fields
import org.tools4j.fixgrep.highlights.Highlight

/**
 * User: ben
 * Date: 7/6/2018
 * Time: 6:39 AM
 */
interface FormattedFields: Fields {
    fun highlight(highlight: Highlight): FormattedFields
}