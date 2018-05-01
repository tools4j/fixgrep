package org.tools4j.fixgrep.help

import joptsimple.HelpFormatter
import joptsimple.OptionDescriptor
import org.tools4j.fixgrep.OptionParserFactory
import org.tools4j.fixgrep.highlights.DefaultHighlightTextEffects
import org.tools4j.fixgrep.highlights.ExampleTable
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.Ansi256Color
/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class OptionsHelp: HelpFormatter {
    override fun format(options: MutableMap<String, out OptionDescriptor>): String {
        helpByOptions.values.stream().distinct().forEach {
            println(it.toString())
            println("\n")
        }

        val buffer = StringBuilder()
        for (each in options.values) {
            buffer.append(sectionFor(each))
        }
        return buffer.toString()
    }

    private fun sectionFor(option: OptionDescriptor): String {
        return "blah"
    }

    val helpByOptions: MutableMap<String, OptionHelp> by lazy {
        val indent = "    "
        val helpByOptions: MutableMap<String, OptionHelp> = LinkedHashMap()

        val str = "NAME\n" +
                "     fixgrep -- fix protocol searcher and formatter\n" +
                "\n" +
                "SYNOPSIS\n" +
                "     fixgrep [options]\n" +
                "\n" +
                "DESCRIPTION\n" +
                "${indent}The following options are available:\n\n" +

        addOptionHelp(helpByOptions, OptionHelp(listOf("a", "tag-annotations"), "Defines the format of annotations to use when printing fields.", """Using the built in fix spec it is possible to annotate FIX tags with information to make them more human readable. e.g. the fix message '35=D^A11=ABC^A55=AUD/USD' can be made much more readable when presented with the tag and/or value descriptions.  e.g. '[MsgType][1m35[22m=[1mD[22m[NEWORDERSINGLE]|[ClOrdID][1m11[22m=[1mABC[22m|[Symbol][1m55[22m=[1mAUD/USD[22m'.  The default format is called 'outsideAnnotations' which means that the annotations sit on the outside of the tag=value pair.  e.g. [tagAnnotation]tag=value[valueAnnotation]. The other way of specifying this format is 'ba', which is the abbreviation of 'before-after', which means the tag annotation is placed _before_ the tag, and the value annotation is placed _after_ the value.  Below are all the possible values for this option.\n\n
      ${bold}format${reset}              | ${bold}example${reset}
      outsideAnnotations  | [MsgType]35=D[NEWORDERSINGLE]
      ba                  | [MsgType]35=D[NEWORDERSINGLE]
      insideAnnotations   | 35[MsgType]=[NEWORDERSINGLE]D
      ab                  | 35[MsgType]=[NEWORDERSINGLE]D
      bb                  | [MsgType]35=[NEWORDERSINGLE]D
      aa                  | 35[MsgType]=D[NEWORDERSINGLE]
      a_                  | 35[MsgType]=D
      _a                  | 35=D[NEWORDERSINGLE]
      b_                  | [MsgType]35=D
      _b                  | 35=[NEWORDERSINGLE]D
      __                  | 35=D
      none                | 35=D"""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("b", "suppress-bold-tags-and-values"), "Suppresses the bold formatting of tags and values. By default tags and values are displayed in bold. E.g.: '[MsgType]\u001B[1m35\u001B[22m=\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m=\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m=\u001B[1mAUD/USD\u001B[22m'.  In some shells highlighting and bold text don't work well together resulting in partially highlighted fields and lines.  On such shells the user may wish to suppress bold tags and values, so that highlighting is not broken.", null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("d", "input-delimiter", "input-delim"), "Defines the FIX delimiter used in the input fix messages.  Default to control character 1, i.e. \\u0001", null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("e", "exclude-tags"), "Tags to exclude from the formatted FIX.", """A comma separated list of tags which should not be displayed in the output 'formatted' fix.
e.g. '--exclude-tags 22,33' would hide tags 22 and 33 from being displayed.  Can be useful for hiding some of
the less 'interesting' fix fields, such as BeginString, BodyLength or Checksum.  Yawn!"""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("h", "highlight", "highlights"), "Highlight fields or lines using color or console text effects.", """Format: ${bold}criteria[:texteffect1][:texteffect2][:scope]${reset} (criteria is mandatory, texteffect and scope are optional.)
${bold}criteria${reset} must start with a tag number, and can optionally have an equals or tilda followed by a string to match the tags value against.  An equals sign '=' performs an exact match.  If just the tag number is specified, the tag and associated value will be highlighted. Multiple tag (and optional equals value) pairs can be chained together into an 'and' statement using the && operator.  'Or' statements are not supported as of yet, as you can just specify another highlight.
${bold}texteffect1 & texteffect2${reset} are optional. It uses ansi escape codes to print display attributes which affect how the text is displayed.  If this is not specified, fixgrep will loop through a predefined set of foreground colors. If specified, it can be of four different formats. 16 color text effects, 256 color text effects, Bold, or a raw escape code.  Whether these escape codes will work will depend on your particular shell environment.
${bold}scope${reset} is option, and can be either 'Field' or 'Line', which controls whether just the field is highlighted, or the whole line.  If not specified, just the Field is highlighted.'
${bold}Specifying (or not specifying) texteffects${reset}
1. Use the defaults!  If no texteffect is specified, fixgrep will loop through a predefined set of foreground colors.  Those being: ${DefaultHighlightTextEffects.toPrettyList()}
1. 16-color text effects - comprise of the ansi 8 colors, and 8 'bright' colors, used as either a foreground text color, or a background color.  You can specify these by using the format [Fg|Bg][Color]. e.g. ${Ansi16ForegroundColor.exampleString()}, ${Ansi16BackgroundColor.exampleString()}
2. 256-color text effects - (lists can be found on the web, such as here: https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit You can specify these using the format [Fg|Bg][num]. e.g. ${Ansi256Color.parse("Fg5").ansiCode}Fg5${reset}, ${Ansi256Color.parse("Fg23").ansiCode}Fg23${reset}, ${Ansi256Color.parse("Bg107").ansiCode}Bg107${reset}, ${Ansi256Color.parse("Bg58").ansiCode}Bg58${reset}.
3. Bold - Just specify 'Bold'
4. Raw Escape Codes - Allows you to specify whatever codes you want. e.g. RGB colors in the format \u001B[38;2;<r>;<g>;<b>m, as long as the texteffect string starts with the escape code followed by a left square bracket, fixgrep will assume you are using a raw escape code.
${bold}Examples:${reset}
${ExampleTable(fix)
                .add("35")
                .add("35:Bold")
                .add("11")
                .add("11:FgWhite:BgBlue")
                .add("11:FgWhite:BgBlue:Field")
                .add("11:FgWhite:BgBlue:Line")
                .add("11:Field")
                .add("11:Line")
                .add("35=D")
                .add("35=D:BgBlue:FgWhite")
                .add("35=D:Fg8")
                .add("35=D:Fg8:Line")
                .add("35=D&&55=AUD")
                .add("35=D&&55=AUD:FgRed")
                .add("35=D&&55=AUD:FgRed:Field")
                .add("35=D&&55=AUD:FgRed:Line")
                .add("35=D&&55=AUD&&11=AB:FgPurple")
                .toString()}
${bold}Some examples specifying multiple highlights (separated by commas):${reset}
${ExampleTable(fix)
                .add("35,55")
                .add("35=D,55=AUD")
                .add("35=D,55=XYZ")
                .add("35,55,11")
                .add("35:Bold,55")
                .add("35=D:Fg8,55=AUD&&11=AB:BgBrightYellow")
                .add("35=D:Fg8:Line,55=AUD:BgBrightGreen")
                .toString()}
"""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("i", "only-include-tags"), "Tags to include in the formatted fix.", """A comma separated list of tags numbers to include in the output FIX.  Any other fields are discarded."""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("l", "line-format"), "The format of each line.", """The line-format is a specification of how each outputted line should be displayed.  The line-format param is free text, and can include any of the following tokens:
            | | ${'$'}{senderToTargetCompIdDirection} | will display the compIds of the current msg. e.g. ABC->DEF |
            | | ${'$'}{msgColor}                      | will 'start' formatting text using the pre-defined color for the particular FIX message type.  At the moment, these colors cannot be configured. |
            | | ${'$'}{msgReset}                      | stops formatting text using the pre-defined color for the FIX message. |
            | | ${'$'}{msgTypeName}                   | displays the FIX msg type name, e.g. NewOrderSingle, CancelRequest.  Execution reports have an additional postfix displaying the exec type, e.g. Exec.Fill, Exec.New  |
            | | ${'$'}{msgFix}                        | displays the formatted FIX message |
            | | ${'$'}{n}                             | displays the value of that fix tag.  e.g. for a NewOrderSingle ${'$'}{35} would print 'D' |
            | | ${'$'}n                               | (note, no braces), will print the captured regex group 'n' from the regex specified in the parameter line-regex (or property line.regex). |
        """.trimMargin()))

        addOptionHelp(helpByOptions, OptionHelp(listOf("m", "include-only-messages-of-type"), "A comma separated list of msg types to display.", "e.g. to display only NewOrderSingles and ExecutionReports, use 35,8"))

        addOptionHelp(helpByOptions, OptionHelp(listOf("n", "no-color", "suppress-colors"), "Suppresses any colorization in lines.", "Note, see the 'suppress-bold-tags-and-values' parameter to also suppress usage of bold text effect on formatted lines"))

        addOptionHelp(helpByOptions, OptionHelp(listOf("o", "output-delimiter", "output-delim"), "Defines the delimiter to print between FIX tags in the formatted output.", null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("r", "line-regex"), "Defines the regex to use, when parsing input lines.", """Although FIX messages appear in some logs as 'pure' FIX, they can also be displayed as part of an application's normal logging, and may have text appearing before/after the FIX message.  Such as timestamps, thread identifiers, etc.  This line can be customized for your application so that fixgrep knows where to find the actual FIX message in each log line.  Use in conjunction with the parameter 'line-regexgroup-for-fix' to tell fixgrep, which regex 'group' to use.  The default value for this is ${bold}^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(\\d+=.*${'$'})${reset}, which looks for an optional ISO timestamp followed by any characters, followed by any number of tag=value pairs which make up the FIX message.  The regex 'group' that contains the FIX message is number 2, as the second set of capturing brackets (\\d+=.*${'$'}) is where we'd expect to find the fix tag.  This default format should match against most variations of FIX logging formats, however you might want to modify it if there is any additional information that you wish to 'capture' and print back out by modifying the line=format parameter, and specifying ${'$'}n tokens.  Or if you wish to optimize the searching.  For example, if your logs contain just FIX messages (no other text), line-regex could be defined as '.*' and line-regexgroup-for-fix defined as zero 0.  (In regex group 0 is a 'special' group which returns the entire match.)"""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("s", "sort-by-tags"), "Defines the preferred order of the FIX tags in the formatted output.", "Let's face it, some tags are more interesting than others.  This parameter allows you to display the more 'interesting' tags at the front of the outputted message."))

        addOptionHelp(helpByOptions, OptionHelp(listOf("x", "line-regexgroup-for-fix"), "Combined with the 'line-regex' parameter, is used to specify which 'capturing group' of the regex contains the actual fix message.", null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("z", "exclude-messages-of-type"), "Comma separated list of msgType codes.  Can be used to hide messages of certain types from being displayed.", "e.g. To 'hide' Logon and Heartbeat messages, this parameter could be defined as 'A,0'"))

        addOptionHelp(helpByOptions, OptionHelp(listOf("?", "help"), "Displays help text", null))

        helpByOptions
    }

    private fun addOptionHelp(helpByOptions: MutableMap<String, OptionHelp>, optionHelp: OptionHelp) {
        for(option in optionHelp.options){
            helpByOptions.put(option, optionHelp)
        }
    }

    class OptionHelp(val options: List<String>, val tagline: String, val description: String?){
        override fun toString(): String {
            val sb = StringBuilder()
            options.forEach {
                if(sb.length > 0) sb.append(", ")
                sb.append(bold)
                sb.append(if(it.length == 1) "-" else "--")
                sb.append(it)
                sb.append(reset)
            }
            sb.append("\n")
            sb.append(tagline).append("\n")
            if(description != null) sb.append(description)
            return sb.toString()
        }
    }

    companion object {
        val bold = "\u001B[1m"
        val reset = "\u001B[0m"
        val fix = "35=D|11=ABC|55=AUD/USD"

        @JvmStatic
        fun main(args: Array<String>) {
            val help = OptionsHelp()
            val optionParser = OptionParserFactory().optionParser
            optionParser.formatHelpWith(help)
            optionParser.printHelpOn(System.out)
        }
    }
}