package org.tools4j.fixgrep.texteffect

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 10/04/2018
 * Time: 6:42 AM
 */
class Ansi16ColorTest extends Specification {
    def "GetAnsiCode"() {
        expect:
        Ansi16Color.parse("BgBlue").consoleTextBefore == "\u001B[44m"
        Ansi16Color.parse("Blue").consoleTextBefore == "\u001B[34m"
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
        Ansi16Color.parse(expression).consoleTextBefore == expectedAnsiCode

        where:
        expression    | expectedAnsiCode
        "BgBrightRed" | Ansi16BackgroundColor.BrightRed.consoleTextBefore
        "Blue"        | Ansi16ForegroundColor.Blue.consoleTextBefore
        "BrightRed"   | Ansi16ForegroundColor.BrightRed.consoleTextBefore
        "FgBlue"      | Ansi16ForegroundColor.Blue.consoleTextBefore
    }
}
