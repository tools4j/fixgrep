package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsAnnotator
import org.tools4j.fix.FixSpec

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:01 PM
 */
class AnnotatingFieldProcessor(val fixSpec: FixSpec, output: FieldsProcessor): AbstractFieldsProcessingPipe(output) {
    override fun accept(fieldsSource: Fields) {
        output.accept(FieldsAnnotator(fixSpec, fieldsSource).fields)
    }
}