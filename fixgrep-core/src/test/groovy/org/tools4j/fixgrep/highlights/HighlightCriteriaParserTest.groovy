package org.tools4j.fixgrep.highlights

import spock.lang.Specification

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:15 PM
 */
class HighlightCriteriaParserTest extends Specification {
    def "Parse"(final String expression, final HighlightCriteria expectedCriteria) {
        given:

        when:
        HighlightCriteria highlightCriteria = new HighlightCriteriaParser().parse(expression)

        then:
        assert highlightCriteria == expectedCriteria

        where:
        expression                   | expectedCriteria
        '35'                         | tagPresent(35)
        '35=ABC'                     | tagMatches(35, 'ABC')
        '35=ABC&&150'                | and(tagMatches(35, 'ABC'), tagPresent(150))
        '35=ABC&&150=1234'           | and(tagMatches(35, 'ABC'), tagMatches(150, '1234'))
        '35=ABC&&150=1234&&44=blah'  | and(tagMatches(35, 'ABC'), tagMatches(150, '1234'), tagMatches(44, 'blah'))
        ''                           | new HighlightCriteriaAlwaysTrue()
    }

    HighlightCriteria tagPresent(int tag){
        return new HighlightCriteriaTagIsPresent(tag)
    }

    HighlightCriteria tagMatches(int tag, String matches){
        return new HighlightCriteriaTagValueMatches(tag, matches)
    }

    HighlightCriteria and(HighlightCriteria ... highlightCriteria){
        return new HighlightCriteriaLogicalAnd(highlightCriteria.toList())
    }
}
