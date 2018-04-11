package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import spock.lang.Specification

/**
 * User: ben
 * Date: 5/04/2018
 * Time: 6:12 PM
 */
class HighlightCriteriaTagIsPresentTest extends Specification {
    def "matches"() {
        expect:
        Fields fields = new FieldsImpl('35=8|150=A', '|')
        HighlightCriteriaTagIsPresent criteria = new HighlightCriteriaTagIsPresent(35)
        def match = criteria.matches(fields)
        assert match.matches
        assert match.matchingFields == [fields.getField(35)]
    }

    def "doesn't match"() {
        expect:
        Fields fields = new FieldsImpl('35=8|150=A', '|')
        HighlightCriteriaTagIsPresent criteria = new HighlightCriteriaTagIsPresent(36)
        assert !criteria.matches(fields).matches
    }

    def "empty fields"() {
        expect:
        Fields fields = new FieldsImpl('', '|')
        HighlightCriteriaTagIsPresent criteria = new HighlightCriteriaTagIsPresent(36)
        assert !criteria.matches(fields).matches
    }
}
