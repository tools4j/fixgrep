package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 7/5/2018
 * Time: 5:42 PM
 */
abstract class FieldsFormatter(val annotationPositions: AnnotationPositions) {
    var tagRaw: Int? = null
    var valueRaw: String? = null
    abstract fun visit(fields: Fields)
    abstract fun finish();
}