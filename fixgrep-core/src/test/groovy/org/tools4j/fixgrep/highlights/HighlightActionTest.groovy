package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors
import static org.tools4j.fixgrep.highlights.HighlightScope.*

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:34 AM
 */
class HighlightActionTest extends Specification {
    private final static String color = Ansi16ForegroundColor.Blue.ansiCode
    private final static String reset = Ansi.Reset.ansiCode

    @Unroll
    def "highlight #matchingTags #scope #fix #expectedOutput"(final List<Integer> matchingTags, final HighlightScope scope, final String fix, final String expectedOutput) {
        given:
        final Fields fields = new FieldsImpl(fix, '|')

        when:
        final List<Field> matchingFields = matchingTags.stream().map{fields.getField(it)}.collect(Collectors.toList())
        final Fields outputFields = new HighlightAction(scope, Ansi16ForegroundColor.Blue).apply(fields, matchingFields)
        final String output = outputFields.toConsoleText('|')
        println output

        then:
        assert output == expectedOutput

         where:
        matchingTags | scope                | fix                       | expectedOutput
        [35]         | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${reset}|150=A|55=AUD/USD"
        [35,55]      | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${reset}|150=A|${color}55=AUD/USD${reset}"
        [35,55,150]  | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${reset}|${color}150=A${reset}|${color}55=AUD/USD${reset}"
        [22]         | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "35=blah|150=A|55=AUD/USD"
        [22]         | HighlightScope.Line  |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${reset}|${color}150=A${reset}|${color}55=AUD/USD${reset}"
    }
}
