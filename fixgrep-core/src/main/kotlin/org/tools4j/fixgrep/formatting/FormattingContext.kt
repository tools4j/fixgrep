package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Fields
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.FixSpecParser
import org.tools4j.fix.spec.MessageSpec

/**
 * User: benjw
 * Date: 9/7/2018
 * Time: 11:02 AM
 */
class FormattingContext(
        val fields: Fields,
        val annotationPositions: AnnotationPositions,
        val boldTagAndValue: Boolean,
        val fixSpec: FixSpecDefinition){

    constructor(fields: Fields): this(fields, AnnotationPositions.OUTSIDE_ANNOTATED, true, FixSpecParser().parseSpec())

    val groupStack = GroupStack()

    val messageSpec: MessageSpec? by lazy {
        fixSpec.messagesByMsgType[fields.getField(35)?.stringValue()]
    }
}