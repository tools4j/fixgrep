package org.tools4j.fix

/**
 * User: ben
 * Date: 30/6/17
 * Time: 5:20 PM
 */
class OutsideAnnotatedSingleLineFormat @JvmOverloads constructor(private val fields: Fields, private val delimiter: Char = '|') {
    constructor(fieldsSource: FieldsSource) : this(fieldsSource.fields)

    val stringValue: String by lazy {
        val sb = StringBuilder()
        for (field in fields) {
            if (sb.length > 0) sb.append(delimiter)
            sb.append(AnnotatedField(field, AnnotationSpec.OUTSIDE_ANNOTATED).toPrettyString())
        }
        sb.toString()
    }

    override fun toString(): String {
        return stringValue
    }
}
