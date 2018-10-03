package org.tools4j.fixgrep;

import org.tools4j.fix.Fields
import java.util.regex.Matcher

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:03 PM
 */

class FixLine(val fields: Fields, val matcher: Matcher) {
    override fun toString(): String {
        return fields.toDelimitedString()
    }
}
