package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/17/2018
 * Time: 5:50 AM
 */
class HighlightedFields(fields: Fields, val textEffect: TextEffect): FieldsImpl(fields) {
}