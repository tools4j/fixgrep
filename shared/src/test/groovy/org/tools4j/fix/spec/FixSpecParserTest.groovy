package org.tools4j.fix.spec

import spock.lang.Specification

/**
 * User: benjw
 * Date: 9/3/2018
 * Time: 6:37 AM
 */
class FixSpecParserTest extends Specification {
    def "test FIX four four"() {
        when:
        final FixSpecDefinition fixSpec = new FixSpecParser('path/to/FIX44.xml').parseSpec()

        then:
        assert fixSpec.fieldsByName.size() == 916
        assert fixSpec.groupsByName.size() == 52
        assert fixSpec.messagesByName.size() == 92

        then:
        final MessageSpec nos = fixSpec.messagesByMsgType.get("D")
        assert nos.fields.size() == 149
    }

    def "test FIX default"() {
        when:
        final FixSpecDefinition fixSpec = new FixSpecParser().parseSpec()

        then:
        assert fixSpec.fieldsByName.size() == 1606
        assert fixSpec.groupsByName.size() == 117
        assert fixSpec.messagesByName.size() == 117

        then:
        final MessageSpec nos = fixSpec.messagesByMsgType.get("D")
        assert nos.fields.size() == 232
    }

    def "test FIX five zero SP two - from working dir"() {
        when:
        final FixSpecDefinition fixSpec = new FixSpecParser('fixgrep-core/src/main/resources/FIX50SP2.xml').parseSpec()

        then:
        assert fixSpec.fieldsByName.size() == 1606
        assert fixSpec.groupsByName.size() == 117
        assert fixSpec.messagesByName.size() == 117

        then:
        final MessageSpec nos = fixSpec.messagesByMsgType.get("D")
        assert nos.fields.size() == 232
    }
}
