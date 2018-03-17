package org.tools4j.fixgrep

import org.tools4j.fix.FieldsSource
import spock.lang.Specification

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:14 PM
 */
class SimpleLabelTest extends Specification {
    def "LabelAndReturnNewLine"() {
        given:
        final Label label = new SimpleLabel(new LabellingCriteria.AlwaysTrueCriteria(), "^", "Fill")

        when:
        final String output = label.labelAndReturnNewLine(new FieldsSource.FixtureNos().fields, "blahblahblah")

        then:
        assert output == 'Fillblahblahblah'
    }
}
