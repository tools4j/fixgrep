package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Delimiter
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: ben
 * Date: 21/05/2018
 * Time: 6:02 PM
 */
class HighlightedDelimiter(override val delimiter: String, val textEffect: TextEffect): Delimiter {
}