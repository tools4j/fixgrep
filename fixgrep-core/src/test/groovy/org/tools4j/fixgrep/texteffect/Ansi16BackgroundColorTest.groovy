package org.tools4j.fixgrep.texteffect

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 10/04/2018
 * Time: 6:25 AM
 */
class Ansi16BackgroundColorTest extends Specification {
    def "test GetAnsiCode"() {
        expect:
        Ansi16BackgroundColor.Red.consoleTextBefore == "\u001B[41m"
    }

    @Unroll
    def "test contains [#expression]"(final String expression, final boolean expectedToContain){
        expect:
        Ansi16BackgroundColor.contains(expression) == expectedToContain

        where :
        expression  | expectedToContain
        "Blue"      | true
        "BrightRed" | true
        "Blah"      | false
        "BgBlue"    | true
        "FgBlue"    | false
    }

    @Unroll
    def "test parse [#expression]"(final String expression, final Ansi16BackgroundColor expectedResult){
        expect:
        Ansi16BackgroundColor.parse(expression) == expectedResult

        where:
        expression  | expectedResult
        "Blue"      | Ansi16BackgroundColor.Blue
        "BrightRed" | Ansi16BackgroundColor.BrightRed
        "BgBlue"    | Ansi16BackgroundColor.Blue
    }

    @Unroll
    def "test parse [#expression] and expect exception"(final String expression){
        when:
        Ansi16BackgroundColor.parse(expression)

        then:
        def throwable = thrown(IllegalArgumentException)
        assert throwable.getMessage() == "No enum constant org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor.$expression"

        where:
        expression  | _
        "Blah"      | _
        "FgBlue"    | _
    }
}
