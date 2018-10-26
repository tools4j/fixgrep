package org.tools4j.fix

import org.tools4j.fix.spec.FixSpecDefinition
import java.util.*

/**
 * User: ben
 * Date: 11/7/17
 * Time: 5:19 PM
 */
class FieldsAnnotator(val inputFields: Fields, val fixSpec: FixSpecDefinition) : FieldsSource {
    val fieldAnnotator = FieldAnnotator(fixSpec)

    override val fields: Fields by lazy {
        val returnFields = ArrayList<Field>()
        for (field in this.inputFields) {
            returnFields.add(fieldAnnotator.getField(field))
        }
        FieldsImpl(returnFields)
    }
}
