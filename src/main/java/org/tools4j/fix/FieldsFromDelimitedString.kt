package org.tools4j.fix

import java.util.ArrayList

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:14 PM
 */
class FieldsFromDelimitedString(private val str: String, private val delimiter: Char) : Fields {

    override val fields: List<Field> by lazy {
        val fields = ArrayList<Field>()
        val fieldStrings = SplitableByCharString(str, delimiter).split().iterator()
        while (fieldStrings.hasNext()) {
            val fieldStr = fieldStrings.next()
            val tagAndValue = SplitableByCharString(fieldStr, '=').splitAtFirst().values()
            val field = Field(tagAndValue!![0], tagAndValue[1])
            fields.add(field)
        }
        fields
    }

    override fun toString(): String {
        return OutsideAnnotatedSingleLineFormat(this, delimiter).toString()
    }
}
