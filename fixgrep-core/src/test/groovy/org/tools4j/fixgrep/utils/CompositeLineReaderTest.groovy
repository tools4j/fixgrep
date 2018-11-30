package org.tools4j.fixgrep.utils

import spock.lang.Specification

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 04:45
 */
class CompositeLineReaderTest extends Specification {
    def "readLines from two Strings"() {
        given:
        String lines1 = "one\ntwo\n\rthree"
        String lines2 = "four\n\rfive"

        when:
        CompositeLineReader lineReader = new CompositeLineReader(lines1, lines2)

        then:
        assert lineReader.readLine() == 'one'
        assert lineReader.readLine() == 'two'
        assert lineReader.readLine() == 'three'
        assert lineReader.readLine() == 'four'
        assert lineReader.readLine() == 'five'
        assert lineReader.readLine() == null
    }
}
