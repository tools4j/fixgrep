package org.tools4j.fix

import java.util.ArrayList

/**
 * User: ben
 * Date: 13/06/2017
 * Time: 6:38 AM
 */
class NonAnnotatedMessageString(private val line: String, private val delimiterRegex: String) : FieldsSource {

    override val fields: Fields by lazy {
        val messageAsSplitString = SplitableByRegexString(line, delimiterRegex).split()
        val fields = ArrayList<Field>()

        for (fieldStr in messageAsSplitString) {
            val fieldAsSplitString: SplitString = SplitableByRegexString(fieldStr, "=").split()
            val tag: String = fieldAsSplitString[0]
            val value = fieldAsSplitString.allElementsOnwards(1, delimiterRegex)
            fields.add(FieldImpl(tag, value!!))
        }
        FieldsImpl(fields)
    }
}
