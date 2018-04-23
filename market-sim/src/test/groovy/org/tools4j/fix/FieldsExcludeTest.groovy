package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 13/04/2018
 * Time: 7:02 AM
 */
class FieldsExcludeTest extends Specification {
    @Shared
    private FixSpec fixSpec = new Fix50SP2FixSpecFromClassPath().load();

    def "test exclude"(List<Integer> exclude, String expectedOutput) {
        given:
        final String fix ="35=8;11=asdf;22=3;55=ABC;99=qwerty"
        final Fields fields = new FieldsImpl(fix, ";")

        when:
        final Fields outputFields = fields.exclude(exclude)

        then:
        assert outputFields.toDelimitedString(";") == expectedOutput

        where:
        exclude             | expectedOutput
        [35]                | "11=asdf;22=3;55=ABC;99=qwerty"
        [99,35]             | "11=asdf;22=3;55=ABC"
        [22,11,55,99,35]    | ""
        []                  | "35=8;11=asdf;22=3;55=ABC;99=qwerty"

    }
}
