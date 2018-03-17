package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import spock.lang.Specification

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 5:37 PM
 */
class TagsMatchCriteriaTest extends Specification {
    def testShouldLabel(final String fieldsStr, final String criteriaStr, final boolean shouldMatch){
        when:
        final Fields fields = new FieldsFromDelimitedString(fieldsStr, (char) '|').fields
        final TagsMatchCriteria criteria = new TagsMatchCriteria(new FieldsFromDelimitedString(criteriaStr, (char) '|').fields.toIntToStringMap())

        then:
        assert criteria.shouldLabel(fields, fields.toDelimitedString((char) '|')) == shouldMatch

        where:
        fieldsStr                   |   criteriaStr                 | shouldMatch
        '35=8|55=AUDUSD|11=ABC'     |   '35=8|55=AUDUSD|11=ABC'     | true
        '35=8|55=AUDUSD'            |   '35=8|55=AUDUSD|11=ABC'     | false
        ''                          |   '35=8|55=AUDUSD|11=ABC'     | false
        '35=8|55=AUDUSD|11=ABC'     |   '35=8|55=AUDUSD'            | true
        '35=8|55=AUDUSD|11=ABC'     |   '35=8'                      | true
        '35=8|55=AUDUSD|11=ABC'     |   ''                          | true
        '35=8|55=AUDUSD|11=ABC'     |   '35=4'                      | false
    }
}
