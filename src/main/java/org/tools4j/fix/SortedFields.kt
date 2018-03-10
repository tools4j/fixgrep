package org.tools4j.fix

import java.util.ArrayList
import java.util.Comparator

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:44 PM
 */
open class SortedFields(private val source: Fields, private val comparator: Comparator<Field>) : Fields {
    override val fields: List<Field> by lazy {
        val fields = ArrayList(source.fields)
        fields.sortWith(comparator)
        fields
    }
}
