package org.tools4j.fix

/**
 * User: ben
 * Date: 20/6/17
 * Time: 6:51 AM
 */
class Fix50SP2FixMessage(private val fixSpec: FixSpec,
                         private val source: Fields,
                         private val commaSeperatedFieldOrder: String) : Fields {

    override val fields: List<Field> by lazy {
        SortedFieldsByConfigList(AnnotatedFields(fixSpec, source), commaSeperatedFieldOrder).fields
    }

    override fun toString(): String {
        return OutsideAnnotatedSingleLineFormat(this).toString()
    }
}
