package org.tools4j.fix

/**
 * User: ben
 * Date: 30/6/17
 * Time: 5:20 PM
 */
class OutsideAnnotatedSingleLineFormat @JvmOverloads constructor(private val fields: Fields, private val delimiter: Char = '|') {
    val stringValue: String by lazy {
        val sb = StringBuilder()
        for (i in 0 until fields.fields.size) {
            if (sb.length > 0) sb.append(delimiter)
            sb.append(fields.fields[i].withOutsideAnnotations)
        }
        sb.toString()
    }

    override fun toString(): String {
        return stringValue
    }
}
