package org.tools4j.fixgrep.texteffect

import spock.lang.Specification
import spock.lang.Unroll
import static org.tools4j.fixgrep.texteffect.AnsiForegroundBackground.*

/**
 * User: ben
 * Date: 10/04/2018
 * Time: 6:25 AM
 */
class Ansi256ColorTest extends Specification {
    def "test GetAnsiCode"() {
        expect:
        new Ansi256Color(123, FOREGROUND).getConsoleTextBefore == "\u001B[38;5;123m"
        new Ansi256Color(123, BACKGROUND).getConsoleTextBefore == "\u001B[48;5;123m"
    }

    @Unroll
    def "test contains [#expression]"(final String expression, final boolean expectedToContain){
        expect:
        Ansi256Color.contains(expression) == expectedToContain

        where :
        expression  | expectedToContain
        "Blue"      | false
        "Fg123"     | true
        "Bg123"     | true
        "123"       | true
    }

    @Unroll
    def "test parse [#expression]"(final String expression, final Ansi256Color expectedResult){
        expect:
        Ansi256Color.parse(expression) == expectedResult

        where:
        expression  | expectedResult
        "Fg123"     | new Ansi256Color(123, FOREGROUND)
        "Bg123"     | new Ansi256Color(123, BACKGROUND)
        "123"       | new Ansi256Color(123, FOREGROUND)
    }

    @Unroll
    def "test parse [#expression] and expect exception"(final String expression){
        when:
        Ansi256Color.parse(expression)

        then:
        def throwable = thrown(IllegalArgumentException)
        assert throwable.getMessage() == "Cannot parse value for Ansi256Color from [$expression]"

        where:
        expression  | _
        "Blah"      | _
        "BgBlue"    | _
    }
}
