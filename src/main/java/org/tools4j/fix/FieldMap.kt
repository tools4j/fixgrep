package org.tools4j.fix

import java.util.HashMap

/**
 * User: ben
 * Date: 9/8/17
 * Time: 5:50 PM
 */
class FieldMap(private val fieldsInput: Fields) : Fields by fieldsInput {
    val mapValue: Map<Number, Field> by lazy {
        val map = HashMap<Number, Field>()
        for (field in fieldsInput.fields) {
            map[field.tag.tag] = field
        }
        map
    }
}
