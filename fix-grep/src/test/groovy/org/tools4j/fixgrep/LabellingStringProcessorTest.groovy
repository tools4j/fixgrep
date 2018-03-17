package org.tools4j.fixgrep

import org.tools4j.fix.FieldsSource
import spock.lang.Specification

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:25 PM
 */
class LabellingStringProcessorTest extends Specification {
    def "Accept"() {
        val lines = [
                new FieldsSource.FixtureNos().fields.toDelimitedString((char) '|'),
                new FieldsSource.FixturePendingNew().fields.toDelimitedString((char) '|'),
                new FieldsSource.FixtureNew().fields.toDelimitedString((char) '|'),
                new FieldsSource.FixtureCancelled().fields.toDelimitedString((char) '|')
        ]


    }
}
