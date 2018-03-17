package org.tools4j.fix

import java.util.ArrayList
import java.util.Comparator

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:44 PM
 */
open class SortedFields(private val source: FieldsSource, private val comparator: Comparator<Field>) : FieldsSource {
    override val fields: Fields by lazy {
        val fields = ArrayList(source.fields)
        fields.sortWith(comparator)
        FieldsImpl(fields)
    }
}
