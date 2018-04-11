package org.tools4j.fixgrep.texteffect

import spock.lang.Specification
import spock.lang.Unroll

import static org.tools4j.fixgrep.texteffect.AnsiForegroundBackground.BACKGROUND
import static org.tools4j.fixgrep.texteffect.AnsiForegroundBackground.FOREGROUND

/**
 * User: ben
 * Date: 10/04/2018
 * Time: 6:25 AM
 */
class TextEffectParserTest extends Specification {
    @Unroll
    def "test parse [#expression]"(final String expression, final TextEffect expectedResult){
        expect:
        def parser = new TextEffectParser()
        assert parser.parse(expression) == expectedResult

        where:
        expression                  | expectedResult
        "\u001b[yo"                 | new TextEffectImpl("\u001b[yo")
        "\u001B[yo"                 | new TextEffectImpl("\u001B[yo")
        "Blue"                      | Ansi16ForegroundColor.Blue
        "FgBlue"                    | Ansi16ForegroundColor.Blue
        "BgBrightBlue"              | Ansi16BackgroundColor.BrightBlue
        "Bg111"                     | new Ansi256Color(111, BACKGROUND)
        "Fg112"                     | new Ansi256Color(112, FOREGROUND)
        "Fg112:Bg33"                | composite(new Ansi256Color(112, FOREGROUND), new Ansi256Color(33, BACKGROUND))
        "Fg112:33"                  | composite(new Ansi256Color(112, FOREGROUND), new Ansi256Color(33, BACKGROUND))
        "112:33"                    | composite(new Ansi256Color(112, FOREGROUND), new Ansi256Color(33, BACKGROUND))
        "Blue:33"                   | composite(Ansi16ForegroundColor.Blue, new Ansi256Color(33, BACKGROUND))
        "116:BrightBlue"            | composite(new Ansi256Color(116, FOREGROUND), Ansi16BackgroundColor.BrightBlue)
        "116:117:118"               | composite(new Ansi256Color(116, FOREGROUND), new Ansi256Color(117, FOREGROUND), new Ansi256Color(118, FOREGROUND))
        "\u001b[foo:\u001b[bar"     | composite(new TextEffectImpl("\u001b[foo"), new TextEffectImpl("\u001b[bar"))
    }

    TextEffect composite(final TextEffect ... textEffects) {
        return new CompositeTextEffect(Arrays.asList(textEffects));
    }

    @Unroll
    def "test parse [#expression] and expect exception"(final String expression, final String expectedExceptionMessage){
        when:
        def parser = new TextEffectParser()
        parser.parse(expression)

        then:
        def throwable = thrown(IllegalArgumentException)
        assert throwable.getMessage() == expectedExceptionMessage

        where:
        expression  | expectedExceptionMessage
        "Blah"      | "Cannot parse textEffect expression [Blah]"
        "Fg112:345" | "Cannot parse textEffect expression [345] of expression [Fg112:345]"
    }
}
