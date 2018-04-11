package org.tools4j.fixgrep.texteffect

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 10/04/2018
 * Time: 6:25 AM
 */
class Ansi16ForegroundColorTest extends Specification {
    def "test GetAnsiCode"() {
        expect:
        Ansi16ForegroundColor.Red.ansiCode == "\u001B[31m"
    }

    @Unroll
    def "test contains [#expression]"(final String expression, final boolean expectedToContain){
        expect:
        Ansi16ForegroundColor.contains(expression) == expectedToContain

        where :
        expression  | expectedToContain
        "Blue"      | true
        "BrightRed" | true
        "Blah"      | false
        "FgBlue"    | true
        "BgBlue"    | false
    }

    @Unroll
    def "test parse [#expression]"(final String expression, final Ansi16ForegroundColor expectedResult){
        expect:
        Ansi16ForegroundColor.parse(expression) == expectedResult

        where:
        expression  | expectedResult
        "Blue"      | Ansi16ForegroundColor.Blue
        "BrightRed" | Ansi16ForegroundColor.BrightRed
        "FgBlue"    | Ansi16ForegroundColor.Blue
    }

    @Unroll
    def "test parse [#expression] and expect exception"(final String expression){
        when:
        Ansi16ForegroundColor.parse(expression)

        then:
        def throwable = thrown(IllegalArgumentException)
        assert throwable.getMessage() == "No enum constant org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor.$expression"

        where:
        expression  | _
        "Blah"      | _
        "BgBlue"    | _
    }
}
