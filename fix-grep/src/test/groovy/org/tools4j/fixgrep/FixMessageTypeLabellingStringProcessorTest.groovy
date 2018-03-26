package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsSource
import spock.lang.Specification

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:25 PM
 */
class FixMessageTypeLabellingStringProcessorTest extends Specification {
    public static final DATE_STR = "2018-03-19T10:12:34.001"

    def "Accept"() {
        given:
        def nosMessage = new FieldsSource.FixtureNos().fields
        def pendingNewMessage = new FieldsSource.FixturePendingNew().fields
        def cancelledMessage = new FieldsSource.FixtureCancelled().fields
        def newMessage = new FieldsSource.FixtureNew().fields

        final MessageStringProcessor.AssertLastReceivedProcessor outputProcessor = new MessageStringProcessor.AssertLastReceivedProcessor()
        final FixMessageTypeLabellingStringProcessor processor = new FixMessageTypeLabellingStringProcessor(outputProcessor = outputProcessor)

        when:
        processor.accept(turnMeIntoLine(nosMessage))

        then:
        assert outputProcessor.lastReceived.toString() == "${DATE_STR} [NewOrderSingle] ${nosMessage.getPipeDelimitedString()}"

        when:
        processor.accept(turnMeIntoLine(pendingNewMessage))

        then:
        assert outputProcessor.lastReceived.toString() == "${DATE_STR} [Exec.PendingNew] ${pendingNewMessage.getPipeDelimitedString()}"

        when:
        processor.accept(turnMeIntoLine(cancelledMessage))

        then:
        assert outputProcessor.lastReceived.toString() == "${DATE_STR} [Exec.Canceled] ${cancelledMessage.getPipeDelimitedString()}"

        when:
        processor.accept(turnMeIntoLine(newMessage))

        then:
        assert outputProcessor.lastReceived.toString() == "${DATE_STR} [Exec.New] ${newMessage.getPipeDelimitedString()}"
    }

    protected SingleLineMessageString turnMeIntoLine(Fields pendingNewMessage) {
        return new SingleLineMessageString(pendingNewMessage, DATE_STR + " " + pendingNewMessage.getPipeDelimitedString())
    }
}
