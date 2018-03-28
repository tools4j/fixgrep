package org.tools4j.fixgrep

import spock.lang.Specification

/**
 * User: ben
 * Date: 28/03/2018
 * Time: 6:21 AM
 */
class AnsiColorTest extends Specification {
    def testColors(){
        expect:
        AnsiColor.values().each {
            println it.ansiCode + it.name() + AnsiColor.Reset
        }
    }
}
