package org.tools4j.fix

/**
 * User: ben
 * Date: 30/6/17
 * Time: 5:20 PM
 */
class NonAnnotatedSingleLineFormat @JvmOverloads constructor(private val fieldsSource: FieldsSource, private val delimiter: String = "|") {
    val stringValue: String by lazy {
        val sb = StringBuilder()
        for (i in 0 until fieldsSource.fields.size) {
            if (sb.length > 0) sb.append(delimiter)
            sb.append(fieldsSource.fields[i])
        }
        sb.toString()
    }

    override fun toString(): String {
        return stringValue
    }
}
