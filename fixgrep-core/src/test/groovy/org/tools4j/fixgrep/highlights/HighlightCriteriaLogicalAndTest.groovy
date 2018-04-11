package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:27 AM
 */
class HighlightCriteriaLogicalAndTest extends Specification {
    @Unroll
    def "matches #tag1 #matchExpr1 #tag2 #matchExpr2 #fix #expectedToMatch"(final int tag1, final String matchExpr1, final int tag2, final String matchExpr2, final String fix, final boolean expectToMatch) {
        given:
        Fields fields = new FieldsImpl(fix, '|')

        when:
        def match = new HighlightCriteriaLogicalAnd(Arrays.asList(
                new HighlightCriteriaTagValueMatches(tag1, matchExpr1),
                new HighlightCriteriaTagValueMatches(tag2, matchExpr2))
        ).matches(fields)

        then:
        assert match.matches == expectToMatch
        if(expectToMatch) {
            assert match.matchingFields == [fields.getField(tag1), fields.getField(tag2)]
        }

        where:
        tag1 | matchExpr1 | tag2 | matchExpr2 | fix                   | expectToMatch
        35   | 'blah'     | 150  | 'ABC'      | '35=blah|150=ABC'     | true
        35   | 'la'       | 150  | 'BC'       | '35=blah|150=ABC'     | true
        35   | 'la'       | 150  | '123'      | '35=blah|150=ABC'     | false
        35   | 'la'       | 222  | 'ABC'      | '35=blah|150=ABC'     | false
        25   | ''         | 222  | 'ABC'      | '35=blah|150=ABC'     | false
    }
}
