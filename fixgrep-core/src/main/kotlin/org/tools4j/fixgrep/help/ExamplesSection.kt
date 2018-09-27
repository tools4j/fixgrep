package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.utils.Constants.Companion.DOLLAR

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 5:50 PM
 */
class ExamplesSection(val docWriterFactory: DocWriterFactory) {
    fun toFormattedText(): String {
        val lines = listOf(
                "35=D|11=C28|55=AUD/USD|54=2|38=1464820|44=100.026",
                "35=8|11=C28|150=0|151=1464820|14=0|44=100.02",
                "35=G|11=C32|38=1465320|40=2|44=100.12",
                "35=8|11=C32|150=5|151=1465320|14=0|44=100.12",
                "35=8|11=C32|150=2|151=1072490|14=392830|44=100.00")

        val docWriter = docWriterFactory.createNew()
        docWriter
                .writeHeading(1, "Examples")
                .writeLn("Examples are being applied to the following lines of fix.  I have purposefully over simplied these FIX messages and stripped them down so that they might fit horizontally on a console: ")
                .startSection(MiscTextEffect.Console)
        lines.forEach { docWriter.startSection(HtmlOnlyTextEffect("line")).write(it).endSection() }
        docWriter.endSection()

        var examplesList = ExamplesList(lines, docWriter)
        examplesList.add("<no arguments>", "This is how fix messages will appear by default by fixgrep.  Notice that the date has been stripped from the front of each message.  See later examples on how to add this date back in.")
        examplesList.add("--exclude-tags 55,11", "Exclude tags 55 and 11.")
        examplesList.add("-e 55,11", "Same configuration to exclude tags 55,11 but using short form option -e")
        examplesList.add("--exclude-messages-of-type 8", "Excludes messages of type 8 (ExecutionReport).")
        examplesList.add("-v 8", "Same exclusion of ExecutionReports using the 'short form' option v")
        examplesList.add("-v D", "Excluding messages of type D (NewOrderSingle)")
        examplesList.add("-v D,8", "Excluding both messages of type D (NewOrderSingle) and of type 8 (ExecutionReport)")
        examplesList.add("-v D -v 8", "Same exclusion, but using repeated options.")
        examplesList.add("--tag-annotations outsideAnnotated", "Set annotations to 'outside' meaning the annotated tag will sit before the number, and the annotated value will sit after the valueRaw.  Like this [tagAnnotation]number=valueRaw[valueAnnotation], e.g. [Side]54=2[SELL].  This is the default annotation format used by fixgrep, so don't be surprised if the formatted fix did not change.")
        examplesList.add("-a outsideAnnotated", "Same, but using the short form option 'a'")
        examplesList.add("-a ba", "Same, but using abbreviated form 'ba' meaning 'before-after'")
        examplesList.add("-a insideAnnotated", "Inside annotation, meaning the annotated tag will sit after the number, and the annotated value will sit before the valueRaw.  Like this number[tagAnnotation]=[valueAnnotation]valueRaw, e.g. 54[Side]=[SELL]2")
        examplesList.add("-a ab", "Same as 'insideAnnotated' but specified in abbreviated form 'ab' meaning after-before.")
        examplesList.add("-a b_", "Annotations set to before-none, meaning only Tag annotations will be printed, before the raw tag value.")
        examplesList.add("-a a_", "Annotations set to after-none, meaning only Tag annotations will be printed, after the raw tag value.")
        examplesList.add("-a _b", "Annotations set to none-before, meaning only Tag annotations will be printed, before the raw tag value.")
        examplesList.add("-a _a", "Annotations set to none-after, meaning only Tag annotations will be printed, after the raw tag value.")
        examplesList.add("-a __", "Configuring NO annotations.")
        examplesList.add("-a none", "Another way of configuring NO annotations.")
        examplesList.add("--include-only-messages-of-type D", "Will only print messages of type D (NewOrderSingle).")
        examplesList.add("-m D", "Same inclusion of just NewOrderSingle messages, but option specified in short form 'm'")
        examplesList.add("-m D,8", "Only show NewOrderSingles and ExecutionReports")
        examplesList.add("--output-delimiter :", "Set output delimiter to ':'.  (Default is pipe '|')")
        examplesList.add("-o :", "Same, but using short form option -o")
        examplesList.add("--sort-by-tags 55,11", "For each fix line, show tags 55 then 11 first if they exist, followed by other tags in the order that they originally appeared.")
        examplesList.add("-s 55,11", "Same configuration, but using short form option -s.")
        examplesList.add("-s 55,11,35", "Show tags 55, then 11, then 35 first on each line, followed by other tags in the order that they originally appeared.")
        examplesList.add("--only-include-tags 35", "Only show tag 35.")
        examplesList.add("-t 35", "Same configuration to only show tag 35, but using short form option -t")
        examplesList.add("-t 35,55", "Only show tags 35 and 55")
        examplesList.add("-t 35,11", "Only show tags 35 and 11")
        examplesList.add("--highlights 35", "Highlight tag 35 irrespective of value.")
        examplesList.add("-h 35", "Same highlight but using short form option -h")
        examplesList.add("-h 35=8", "Highlight tag 35 when it's value is 8 (ExecutionReport)")
        examplesList.add("-h 35=8:Msg", "Highlight whole lines when tag 35=8 (ExecutionReport)")
        examplesList.add("-h 35=8:Bg12:Msg", "Highlight whole lines with background color 3 of the 256 color map, when tag 35=8 (ExecutionReport)")
        examplesList.add("-h 35,55", "Highlight tags 35 and 55.")
        examplesList.add("-h 35:Bg176,55:Bg9", "Highlight tag 35 with a background color of 8.  Highlight tag 55 with a background color of 9.")
        examplesList.add("-h 35=D:Msg,55", "When a message has tag 35=D, highlight the whole line.  Also highlight tag 55.")
        examplesList.add("--suppress-bold-tags-and-values", "Don't use bold text effects in the output fix.")
        examplesList.add("-p", "Same configuration but with short form option -p")
        examplesList.end()

        val lines2 = listOf(
                "2018-05-30T07:12:34.456 Thread-11 gbfix001 35=D|11=C28|55=AUD/USD|54=2|38=1464820|44=100.026",
                "2018-05-30T07:12:35.101 Thread-11 gbfix001 Processing new order single, ClOrderId=11, mid price captured at time 07:12:34.011=100.3",
                "2018-05-30T07:12:35.101 Thread-11 gbfix001 Sending back PendingNew for OrderId=O26 ClOrderId=11",
                "2018-05-30T07:12:35.101 Thread-11 gbfix001 35=8|11=C28|150=0|151=1464820|14=0|44=100.02",
                "2018-05-30T07:22:12.350 Thread-06 gbfix001 35=G|11=C32|38=1465320|40=2|44=100.12",
                "2018-05-30T07:12:35.101 Thread-06 gbfix002 Amend request recieved for OrderId=O26",
                "2018-05-30T07:22:13.670 Thread-01 gbfix002 35=8|11=C32|150=5|151=1465320|14=0|44=100.12",
                "2018-05-30T07:34:34.060 Thread-13 gbfix002 35=8|11=C32|150=2|151=1072490|14=392830|44=100.00")

        examplesList = ExamplesList(lines2, docWriter)

        docWriter.writeHeading(2, "Parsing log lines")
                .writeLn("So far we have looked at examples of options which just change the way the FIX message is presented.  It is also worth considering how fixgrep parses log files, and how parts of that log line (not just the FIX) can be printed in the fixgrep output.")
                .writeLn("It is common for FIX messages to be printed in the same file as application logs.  It is also common to have other things printed on the same line as the FIX message.  Such as timestamps, thread numbers, etc.")
                .writeLn("Let's consider these log lines as input:")

        docWriter.startSection(MiscTextEffect.Console)
        lines2.forEach { docWriter.writeLn(it) }
        docWriter.endSection()

                .writeLn("There is an options (-F --output-format-horizontal-console), which can be used to configure the regular expression that is run against every log line.  By default this is:")
                .writeLn("^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(\\d+=.*$)", MiscTextEffect.Console)
                .writeLn("...which translates to 'look for an optional date at the start of the line, followed by any characters, followed by one or more digits, followed by an equals sign.")
                .writeLn("Parsing our new input lines outputs this:")

        examplesList.add("<no arguments>", "Parsing new log lines with no arguments.")
        examplesList.add("--input-line-format ^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", "Oh no! In the example above, fixgrep assumed the line containing '...mid price captured at time 07:12:34.011=100.3' contained a FIX message because of the '11=100.3' text, it then printed out: '[ClOrdID]11=100.3'.  To remedy this, we can use a more specific regex:")
        examplesList.add("--output-format-horizontal-console ${'$'}1:${'$'}{msgFix} --input-line-format ^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", "That's better.  Now that log line is not picked up.  Let's assume we do wish to show the date at the start of each line.  We do this by specifying ${DOLLAR}1 at the start of the output-format-horizontal-console.")
        examplesList.add("--output-format-horizontal-console ${'$'}1:${'$'}{msgFix} --input-line-format ^\\d{4}-[01]\\d-[0-3]\\d[T\\s]([0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", "Whilst we're at it, let's just print out the time by slightly modifying the position of the first set of capturing brackets, as the whole date becomes a bit redundant.")
        examplesList.add("--output-format-horizontal-console ${DOLLAR}1:${DOLLAR}{msgTypeName}:${'$'}{msgFix} --input-line-format ^\\d{4}-[01]\\d-[0-3]\\d[T\\s]([0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", "We can use another pre-defined tag ${DOLLAR}{msgTypeName} to print out not just the messageType, but also the execType if it's an execution report.")
        examplesList.add("--output-format-horizontal-console ${DOLLAR}1:${'$'}{msgColor}${DOLLAR}{msgTypeName}${'$'}{colorReset}:${'$'}{msgFix} --input-line-format ^\\d{4}-[01]\\d-[0-3]\\d[T\\s]([0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", "And add coloring per message type")
        examplesList.add("--exclude-tagsInId 35 --output-format-horizontal-console ${DOLLAR}1:${'$'}{msgColor}${DOLLAR}{msgTypeName}${'$'}{colorReset}:${'$'}{msgFix} --input-line-format ^\\d{4}-[01]\\d-[0-3]\\d[T\\s]([0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", "I'm going to exclude (hide) field 35 as I already have the MessageType printed at the start of the line.")

        docWriter.writeLn("This is now looking more presentable.  If I want to have these settings applied each time I run fixgrep, I just need to modify my ~/.fixgrep/application.properties file. Fixgrep can automatically create this file by running:")
        docWriter.writeLn("fixgrep --install", MiscTextEffect.Console)
        docWriter.writeLn("And populate this file with the corresponding property settings (notice I've replaced the option dashes, with property dots):")
        docWriter.writeLn(
                "exclude.tagsInId=35\n" +
                "output.format.horizontal.console=${DOLLAR}1:${'$'}{msgColor}${DOLLAR}{msgTypeName}${'$'}{colorReset}:${'$'}{msgFix}\n" +
                "input.line.format=^\\d{4}-[01]\\d-[0-3]\\d[T\\s]([0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(35=.*\$)", MiscTextEffect.Console)

        examplesList.end()

        docWriter.writeHeading(2, "Vertical formatting")
                .writeLn("So far we've looked at examples using horizontal formatting.  Sometimes vertical formatting is preferable.  Especially when looking at messages containing repeating groups such as prices.")
                .writeLn("Let's consider this single log lines as input.  It contains 3 repeating prices:")

        val lines3 = listOf(
                "35=X|262=ABCD|268=3|279=0|269=0|55=AUD/USD|270=1.12345|279=0|269=1|55=AUD/USD|270=1.12355|279=0|269=1|55=AUD/USD|270=1.12355|1022=FeedA|"
        )

        docWriter.startSection(MiscTextEffect.Console)
        lines3.forEach { docWriter.writeLn(it) }
        docWriter.endSection()

        examplesList = ExamplesList(lines3, docWriter)
        examplesList.add("-V", "The default vertical format is 'non-aligned', which has the benefit that repeating groups can be indented.")
        examplesList.add("-V -G false", "To turn off repeating group indenting, set the -G parameter to 'false'.")
        examplesList.add("-V -A", "And to use 'aligned' vertical formatting, use the -A flag.  Note that when using 'aligned' vertical formatting, you will not be able to use indented repeating groups.")
        examplesList.end()

        return docWriter.toFormattedText()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(ExamplesSection(DocWriterFactory.ConsoleText).toFormattedText())
        }
    }
}