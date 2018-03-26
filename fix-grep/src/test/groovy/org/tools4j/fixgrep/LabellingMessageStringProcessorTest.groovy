package org.tools4j.fixgrep

import org.tools4j.fix.FieldsSource
import spock.lang.Specification

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:25 PM
 */
class LabellingMessageStringProcessorTest extends Specification {
    def "Accept"() {
        given:
        def nosMessage = new FieldsSource.FixtureNos().fields
        def pendingNewMessage = new FieldsSource.FixturePendingNew().fields

        final MessageStringProcessor.AssertLastReceivedProcessor outputProcessor = new MessageStringProcessor.AssertLastReceivedProcessor()
        final List<Label> labels = new Label.SimpleLabelsWithReplaceAfterOptionalDate().get()
        LabellingMessageStringProcessor processor = new LabellingMessageStringProcessor(labels, outputProcessor)
        def dateStr = "2018-03-19T10:12:34.001"

        when:
        processor.accept(new SingleLineMessageString(nosMessage, dateStr + " " + nosMessage.getPipeDelimitedString()))

        then:
        assert outputProcessor.lastReceived.toString() == "$dateStr [NewOrderSingle] ${nosMessage.getPipeDelimitedString()}"

        when:
        processor.accept(new SingleLineMessageString(pendingNewMessage, dateStr + " " + pendingNewMessage.getPipeDelimitedString()))

        then:
        assert outputProcessor.lastReceived.toString() == "$dateStr [PendingNew] ${pendingNewMessage.getPipeDelimitedString()}"
    }
}
