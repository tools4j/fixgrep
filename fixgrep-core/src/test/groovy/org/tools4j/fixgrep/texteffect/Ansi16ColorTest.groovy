package org.tools4j.fixgrep.texteffect

import spock.lang.Specification
import spock.lang.Unroll
import static org.tools4j.fixgrep.texteffect.AnsiForegroundBackground.*

/**
 * User: ben
 * Date: 10/04/2018
 * Time: 6:42 AM
 */
class Ansi16ColorTest extends Specification {
    def "GetAnsiCode"() {
        expect:
        Ansi16Color.parse("BgBlue").ansiCode == "\u001B[44m"
        Ansi16Color.parse("Blue").ansiCode == "\u001B[34m"
    }

    @Unroll
    def "test contains [#expression]"(final String expression, final boolean expectedToContain){
        expect:
        Ansi16Color.contains(expression) == expectedToContain

        where :
        expression  | expectedToContain
        "Blue"      | true
        "BrightRed" | true
        "Blah"      | false
        "BgBlue"    | true
        "FgBlue"    | true
    }

    @Unroll
    def "test parse [#expression]"(final String expression, final String expectedAnsiCode){
        expect:
        Ansi16Color.parse(expression).ansiCode == expectedAnsiCode

        where:
        expression    | expectedAnsiCode
        "BgBrightRed" | Ansi16BackgroundColor.BrightRed.ansiCode
        "Blue"        | Ansi16ForegroundColor.Blue.ansiCode
        "BrightRed"   | Ansi16ForegroundColor.BrightRed.ansiCode
        "FgBlue"      | Ansi16ForegroundColor.Blue.ansiCode
    }
}
