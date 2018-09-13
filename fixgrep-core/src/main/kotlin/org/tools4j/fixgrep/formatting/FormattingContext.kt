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
        val indentGroupRepeats: Boolean,
        val fixSpec: FixSpecDefinition){

    constructor(fields: Fields): this(fields, AnnotationPositions.OUTSIDE_ANNOTATED, true, true, FixSpecParser().parseSpec())
    constructor(fields: Fields, annotationPositions: AnnotationPositions, boldTagAndValue: Boolean, fixSpec: FixSpecDefinition): this(fields, annotationPositions, boldTagAndValue, true, fixSpec)

    val groupStack: GroupStack by lazy {
        GroupStack(messageSpec)
    }

    val messageSpec: MessageSpec? by lazy {
        val messageTypeCode = fields.getField(35)?.value?.valueRaw
        if(messageTypeCode == null) null else fixSpec.messagesByMsgType[messageTypeCode]
    }
}