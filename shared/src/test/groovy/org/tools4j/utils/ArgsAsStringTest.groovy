package org.tools4j.utils

import spock.lang.Specification

/**
 * User: ben
 * Date: 19/04/2018
 * Time: 6:45 AM
 */
class ArgsAsStringTest extends Specification {
    def "ToArgs"(final String str, List<String> expectedArgs) {
        when:
        final ArgsAsString argsAsString = new ArgsAsString(str)

        then:
        assert argsAsString.toArgs() == expectedArgs

        where:
        str                 | expectedArgs
        'blah'              | ['blah']
        'blah d blah'       | ['blah','d','blah']
        'blah "d blah"'     | ['blah','d blah']
        '"my blah" "d blah"'| ['my blah','d blah']
    }
}
