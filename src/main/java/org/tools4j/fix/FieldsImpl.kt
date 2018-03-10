package org.tools4j.fix

import sun.reflect.misc.FieldUtil.getFields
import java.util.ArrayList

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:43 PM
 */
class FieldsImpl(fields: List<Field>) : Fields {
    override val fields: List<Field> by lazy {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (field in fields) {
            sb.append(field.withOutsideAnnotations)
            sb.append("|")
        }
        return sb.toString()
    }
}
