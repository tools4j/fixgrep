package org.tools4j.fix

import java.util.ArrayList
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 31/8/17
 * Time: 5:14 PM
 */
class FieldsFromDelimitedString(private val str: String, private val inputDelimiter: String) : FieldsSource {

    constructor(str: String) : this(str, Ascii1Char().toString() )

    override val fields: Fields by lazy {
        val fields = ArrayList<Field>()
        val split = str.split(inputDelimiter)
        if(split.size > 0 && !(split.size == 1 && split[0].isEmpty())) {
            val fieldStrings = split.iterator()
            while (fieldStrings.hasNext()) {
                val fieldStr = fieldStrings.next()
                if(fieldStr.isEmpty()) continue;
                val tagAndValue = SplitableByCharString(fieldStr, '=').splitAtFirst().values()
                val field = FieldImpl(tagAndValue!![0], tagAndValue[1])
                fields.add(field)
            }
        }
        FieldsImpl(fields)
    }

    override fun toString(): String {
        return fields.toDelimitedString("|");
    }
}
