package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 5/04/2018
 * Time: 6:25 PM
 */
class HighlightCriteriaTagValueContainsTest extends Specification {
    @Unroll
    def "matches #tag #matchExpr #fix #expectedToMatch"(final int tag, final String matchExpr, final String fix, final boolean expectToMatch) {
        given:
        Fields fields = new FieldsImpl(fix, '|')

        when:
        def match = new HighlightCriteriaTagValueContains(tag, matchExpr).matches(fields)

        then:
        assert match.matches == expectToMatch
        if(expectToMatch) {
            assert match.matchingFields == [fields.getField(tag)]
        }

        where:
        tag | matchExpr | fix                   | expectToMatch
        35  | 'blah'    | '35=blah|150=A'       | true
        35  | 'la'      | '35=blah|150=A'       | true
        35  | 'b'       | '35=blah|150=A'       | true
        35  | 'z'       | '35=blah|150=A'       | false
        35  | 'l'       | '35=blah|150=A'       | true
        35  | ''        | '35=blah|150=A'       | false
        35  | ''        | '35=|150=A'           | true
        66  | 'asdf'    | '35=|150=A'           | false
        66  | 'asdf'    | ''                    | false
        66  | ''        | ''                    | false
    }
}
