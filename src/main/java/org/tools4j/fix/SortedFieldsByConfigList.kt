package org.tools4j.fix

import java.util.Comparator

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:47 PM
 */
class SortedFieldsByConfigList(source: Fields, configListAsCommaSeparatedTagValues: String)
        : SortedFields(source, SortedFieldComparator(SplitableByRegexString(configListAsCommaSeparatedTagValues, ","))) {

    class SortedFieldComparator constructor(private val requiredOrderSplitableString: SplitableByRegexString) : Comparator<Field> {
        override fun compare(one: Field, two: Field): Int {
            val requiredOrder = requiredOrderSplitableString.split()
            val oneIndex = if (requiredOrder.contains("" + one.tag.tag)) requiredOrder.indexOf("" + one.tag.tag) else Integer.MAX_VALUE
            val twoIndex = if (requiredOrder.contains("" + two.tag.tag)) requiredOrder.indexOf("" + two.tag.tag) else Integer.MAX_VALUE
            return Integer.compare(oneIndex, twoIndex)
        }
    }

    override fun toString(): String {
        return OutsideAnnotatedSingleLineFormat(this).toString()
    }
}