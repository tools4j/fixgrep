package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Field
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.formatting.FieldsFormatterHorizontalConsoleText
import org.tools4j.fixgrep.formatting.FieldsFormatterHorizontalHtml
import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:34 AM
 */
class HighlightActionTest extends Specification {
    private final static String color = Ansi16ForegroundColor.Blue.consoleTextBefore
    private final static String normal = Ansi.Reset

    @Unroll
    def "ConsoleText highlight #matchingTags #scope #fix #expectedOutput"(final List<Integer> matchingTags, final HighlightScope scope, final String fix, final String expectedOutput) {
        given:
        final Fields fields = new FieldsImpl(fix, "|")

        when:
        final List<Field> matchingFields = matchingTags.stream().map{fields.getField(it)}.collect(Collectors.toList())
        final Fields outputFields = new HighlightAction(scope, Ansi16ForegroundColor.Blue).apply(fields, matchingFields)
        final String output = new FieldsFormatterHorizontalConsoleText('|').toFormattedText(outputFields)
        println output

        then:
        assert output == expectedOutput

         where:
        matchingTags | scope                | fix                       | expectedOutput
        [35]         | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}|150=A|55=AUD/USD"
        [35,55]      | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}|150=A|${color}55=AUD/USD${normal}"
        [35,55,150]  | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}|${color}150=A${normal}|${color}55=AUD/USD${normal}"
        [22]         | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "35=blah|150=A|55=AUD/USD"
        [22]         | HighlightScope.Line  |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}${color}|${normal}${color}150=A${normal}${color}|${normal}${color}55=AUD/USD${normal}"
    }

    @Unroll
    def "html highlight #matchingTags #scope #fix #expectedOutput"(final List<Integer> matchingTags, final HighlightScope scope, final String fix, final String expectedOutput) {
        given:
        final Fields fields = new FieldsImpl(fix, '|')

        when:
        final List<Field> matchingFields = matchingTags.stream().map{fields.getField(it)}.collect(Collectors.toList())
        final Fields outputFields = new HighlightAction(scope, Ansi16ForegroundColor.Blue).apply(fields, matchingFields)
        final String output = new FieldsFormatterHorizontalHtml('|').toFormattedText(outputFields)
        println output

        then:
        assert output == expectedOutput

        where:
        matchingTags | scope                | fix                       | expectedOutput
        [35]         | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        [35,55]      | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field FgBlue'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        [35,55,150]  | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field FgBlue'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field FgBlue'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        [22]         | HighlightScope.Field |'35=blah|150=A|55=AUD/USD' | "<span class='fields'><span class='field'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        [22]         | HighlightScope.Line  |'35=blah|150=A|55=AUD/USD' | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim FgBlue'>|</span><span class='field FgBlue'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim FgBlue'>|</span><span class='field FgBlue'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
    }
}
