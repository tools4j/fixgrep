package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import spock.lang.Specification

/**
 * User: ben
 * Date: 23/05/2018
 * Time: 6:50 AM
 */
class ConsoleTextTableBuilderTest extends Specification {
    def "GetTable"() {
        final StringWriter stringWriter = new StringWriter();
        final ConsoleTextWriter writer = new ConsoleTextWriter()

        when:
        writer.writeHeading(1, "my table")
        writer.addTable()
            .startNewRow().addTableHeaderCell("one").addTableHeaderCell("two").addTableHeaderCell("three")
            .startNewRow().addCell("four").addCell("five").addCell("")
            .startNewRow().addCell("once").addCell("I").addCell("caught", MiscTextEffect.Console)
            .startNewRow().addCell("a").addCell("fish").addCell("alive")
            .startNewRow().addCell("six", Ansi16BackgroundColor.Yellow).addCell("seven").addCell("eight")
            .startNewRow().addCell("nine").addCell("ten").addCell("then I let him go again", Ansi16ForegroundColor.Blue)
            .endTable()
        def text = writer.toFormattedText()
        println text

        then:
        assert text == "\u001B[1m========================================================================================================\u001B[0m\n" +
                "\u001B[1mMY TABLE\u001B[0m\n" +
                "\u001B[1m========================================================================================================\u001B[0m\n" +
                "|\u001B[1m one  \u001B[22m|\u001B[1m two   \u001B[22m|\u001B[1m three                   \u001B[22m|\n" +
                "| four | five  |                         |\n" +
                "| once | I     | caught                  |\n" +
                "| a    | fish  | alive                   |\n" +
                "|\u001B[43m six  \u001B[0m| seven | eight                   |\n" +
                "| nine | ten   |\u001B[34m then I let him go again \u001B[0m|\n"
    }
}
