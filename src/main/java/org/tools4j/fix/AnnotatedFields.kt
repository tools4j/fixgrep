package org.tools4j.fix

import java.util.ArrayList

/**
 * User: ben
 * Date: 11/7/17
 * Time: 5:19 PM
 */
class AnnotatedFields(private val fixSpec: FixSpec, private val inputFields: Fields) : Fields {
    override val fields: List<Field> by lazy {
        val returnFields = ArrayList<Field>()
        for (field in this.inputFields.fields) {
            returnFields.add(fixSpec.getField(field.tag.tag, field.value.rawValue))
        }
        returnFields
    }

    override fun toString(): String {
        return toString('|')
    }

    fun toString(delimiter: Char): String {
        val sb = StringBuilder()
        fields.joinTo(sb, delimiter+"")
        return sb.toString()
    }
}
