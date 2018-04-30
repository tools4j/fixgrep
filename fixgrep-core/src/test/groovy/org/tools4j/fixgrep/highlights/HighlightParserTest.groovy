package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.Ansi256Color
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:34 AM
 */
class HighlightParserTest extends Specification {
    private final static String FgBlue = Ansi16ForegroundColor.Blue.ansiCode
    private final static String BgRed = Ansi16BackgroundColor.Red.ansiCode
    private final static String Fg2 = Ansi256Color.parse("Fg2").ansiCode
    private final static String Bg4 = Ansi256Color.parse("Bg4").ansiCode
    private final static String Bold = MiscTextEffect.Bold.ansiCode
    private final static String reset = Ansi.Reset.ansiCode

    @Unroll
    def "test parse #expression"(final String expression, final String expectedOutput) {
        given:
        final String fix = '35=blah|150=A|55=AUD/USD'
        final Fields fields = new FieldsImpl(fix, '|')

        when:
        final Highlight highlight = new HighlightParser().parse(expression)
        final Fields highlightedFields = highlight.apply(fields)
        final String highlightedMessage = highlightedFields.toPrettyString('|')
        println "Expected output: $expectedOutput"
        println "Actual output: $highlightedMessage"
        then:
        assert highlightedMessage == expectedOutput

         where:
        expression | expectedOutput
        '35:Blue:Field' | "${FgBlue}35=blah${reset}|150=A|55=AUD/USD"
        '35=foo:Blue:Field' | "35=blah|150=A|55=AUD/USD"
        '35:Bold:Field' | "${Bold}35=blah${reset}|150=A|55=AUD/USD"
        '35=blah:Blue:Field' | "${FgBlue}35=blah${reset}|150=A|55=AUD/USD"
        '35=blah&&150=A:Blue:Red:Field' | "${FgBlue}${BgRed}35=blah${reset}|${FgBlue}${BgRed}150=A${reset}|55=AUD/USD"
        '35=blah&&150=A:Blue:Red:Line' | "${FgBlue}${BgRed}35=blah${reset}|${FgBlue}${BgRed}150=A${reset}|${FgBlue}${BgRed}55=AUD/USD${reset}"
        '35=blah&&150=A:Bold:Red:Line' | "${Bold}${BgRed}35=blah${reset}|${Bold}${BgRed}150=A${reset}|${Bold}${BgRed}55=AUD/USD${reset}"
        '150:Bg4,150=A:Fg2:Line,35=blah&&55~AUD:Blue:Red:Field' | "${Fg2}${FgBlue}${BgRed}35=blah${reset}|${Bg4}${Fg2}150=A${reset}|${Fg2}${FgBlue}${BgRed}55=AUD/USD${reset}"
        '150:Bg4,150=A:Fg2:Line,35=blah&&55~AUD&&150:Blue:Red:Field' | "${Fg2}${FgBlue}${BgRed}35=blah${reset}|${Bg4}${Fg2}${FgBlue}${BgRed}150=A${reset}|${Fg2}${FgBlue}${BgRed}55=AUD/USD${reset}"
    }
}
