package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.Ansi256Color
import org.tools4j.fixgrep.texteffect.AnsiForegroundBackground
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect

/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class OptionsHelp(val docWriterFactory: DocWriterFactory) {

    fun toFormattedText(): String{
        val sb = StringBuilder(docWriterFactory.createNew().writeHeading(1, "Options").toFormattedText())
        helpByOptions.values.stream().distinct().forEach{sb.append(it.toFormattedText())}
        return sb.toString()
    }

    val optionsThatDoNotNeedHelpItems: List<String> by lazy {
        listOf("?", "help", "[arguments]", "install-dir")
    }

    val longestOptions: List<String> by lazy {
        helpByOptions.values.map { it.longestOptionVariation }.toSet().toList()
    }

    val helpByPropertyValues: Map<String, OptionHelp> by lazy {
        helpByOptions.filter { it.value.longestOptionVariation == it.key }
                .filter { !optionsThatDoNotNeedHelpItems.contains(it.key) }
                .mapKeys {it.value.optionVariations.longestAsPropertyKey}
    }

    val helpByOptions: Map<String, OptionHelp> by lazy {
        val helpByOptions: MutableMap<String, OptionHelp> = LinkedHashMap()

        addOptionHelp(helpByOptions, OptionHelp(
                listOf("a", "tag-annotations"),
                "Defines the format of annotations to use when printing fields.",
                "outsideAnnotations",
                docWriterFactory.createNew().write("Using the built in fix spec it is possible to annotate FIX tags with information to make them more human readable. e.g. the fix message '35=D^A11=ABC^A55=AUD/USD' can be made much more readable when presented with the tag and/or value descriptions.  e.g. '[MsgType]")
                        .writeBold("35=D")
                        .write("[NEWORDERSINGLE]|[ClOrdID]")
                        .writeBold("11=ABC")
                        .write("|[Symbol]")
                        .writeBold("55=AUD/USD")
                        .writeLn("'.  The default format is called 'outsideAnnotations' which means that the annotations sit on the outside of the tag=value pair.  e.g. [tagAnnotation]tag=value[valueAnnotation]. The other way of specifying this format is 'ba', which is the abbreviation of 'before-after', which means the tag annotation is placed _before_ the tag, and the value annotation is placed _after_ the value.")
                        .writeBoldLn("All the possible values for this option:")
                .addTable()
                .startNewRow().addTableHeaderCell("format").addTableHeaderCell("example")
                .startNewRow().addCell("outsideAnnotations").addCell("[MsgType]35=D[NEWORDERSINGLE]")
                .startNewRow().addCell("ba").addCell("[MsgType]35=D[NEWORDERSINGLE]")
                .startNewRow().addCell("insideAnnotations").addCell("35[MsgType]=[NEWORDERSINGLE]D")
                .startNewRow().addCell("ab").addCell("35[MsgType]=[NEWORDERSINGLE]D")
                .startNewRow().addCell("bb").addCell("[MsgType]35=[NEWORDERSINGLE]D")
                .startNewRow().addCell("aa").addCell("35[MsgType]=D[NEWORDERSINGLE]")
                .startNewRow().addCell("a_").addCell("35[MsgType]=D")
                .startNewRow().addCell("_a").addCell("35=D[NEWORDERSINGLE]")
                .startNewRow().addCell("b_").addCell("[MsgType]35=D")
                .startNewRow().addCell("_b").addCell("35=[NEWORDERSINGLE]D")
                .startNewRow().addCell("__").addCell("35=D")
                .startNewRow().addCell("none").addCell("35=D")
                .endTable()
                .toFormattedText()))

        addOptionHelp(helpByOptions, OptionHelp(listOf("d", "input-delimiter", "input-delim"), "Defines the FIX delimiter used in the input fix messages.  Default to control character 1, i.e. \\u0001", ":", null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("e", "exclude-tags"), "Tags to exclude from the formatted FIX.", "22,33", """A comma separated list of tags which should not be displayed in the output 'formatted' fix.
e.g. '--exclude-tags 22,33' would hide tags 22 and 33 from being displayed.  Can be useful for hiding some of
the less 'interesting' fix fields, such as BeginString, BodyLength or Checksum.  Yawn!"""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("f", "to-file"), "Send output to a file.", "output.txt", """Filename is optional.  If no filename is specified, then a random file-name will be generated, with the prefix 'fixgrep-'.  And an extension of '.log' if in normal console mode, or an extension of '.html' if output is in html."""))

        addOptionHelp(helpByOptions, OptionHelp(
                listOf("h", "highlight", "highlights"), 
                "Highlight fields or lines using color or console text effects.", 
                "35,55",
                docWriterFactory.createNew()
                .write("Format: ").writeBold("criteria[:texteffect1][:texteffect2][:scope]")
                .writeLn(" (criteria is mandatory, texteffect and scope are optional.)")
                .writeHeading(2, "criteria")
                .writeLn(" must start with a tag number, and can optionally have an equals or tilda followed by a string to match the tags value against.  An equals sign '=' performs an exact match.  If just the tag number is specified, the tag and associated value will be highlighted. Multiple tag (and optional equals value) pairs can be chained together into an 'and' statement using the && operator.  'Or' statements are not supported as of yet, as you can just specify another highlight.")
                .writeHeading(2, "texteffect1 & texteffect2")
                .writeLn(" are optional. It uses ansi escape codes to print display attributes which affect how the text is displayed.  If this is not specified, fixgrep will loop through a predefined set of foreground colors. If specified, it can be of four different formats. 16 color text effects, 256 color text effects, Bold, or a raw escape code.  Whether these escape codes will work will depend on your particular shell environment.")
                .writeHeading(2, "scope")
                .writeLn(" is optional, and can be either 'Field' or 'Line', which controls whether just the field is highlighted, or the whole line.  If not specified, just the Field is highlighted.'")
                .writeHeading(2, "Specifying (or not specifying) texteffects")
                .writeLn("Use the defaults!  If no texteffect is specified, fixgrep will loop through a predefined set of foreground colors.  Those being: ").writeListOfDefaultColors()
                .writeLn("16-color text effects - comprise of the ansi 8 colors, and 8 'bright' colors, used as either a foreground text color, or a background color.  You can specify these by using the format [Fg|Bg][Color]. e.g.")
                .writeListOfAnsi16ForegroundColors()
                .write(" and ")
                .writeListOfAnsi16BackgroundColors()
                .writeLn("256-color text effects - (lists can be found on the web, such as here: https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit You can specify these using the format [Fg|Bg][num]. e.g. ")
                .write("Fg5", Ansi256Color(5, AnsiForegroundBackground.FOREGROUND)).write(", ")
                .write("Fg23", Ansi256Color(23, AnsiForegroundBackground.FOREGROUND)).write(", ")
                .write("Bg107", Ansi256Color(107, AnsiForegroundBackground.BACKGROUND)).write(", ")
                .write("Bg58", Ansi256Color(58, AnsiForegroundBackground.BACKGROUND)).writeLn()
                .writeBoldLn("Raw Escape Codes").writeLn(" - Allows you to specify whatever codes you want. e.g. RGB colors in the format <pre>\u001B[38;2;<r>;<g>;<b>m</pre>, as long as the texteffect string starts with the escape code followed by a left square bracket, fixgrep will assume you are using a raw escape code.")
                .writeHeading(2, "Examples:")
                .writeFormatExamplesTable(fix)
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
                .endTable()
                .writeHeading(2,"Some examples specifying multiple highlights (separated by commas):")
                .writeFormatExamplesTable(fix)
                .add("35,55")
                .add("35=D,55=AUD")
                .add("35=D,55=XYZ")
                .add("35,55,11")
                .add("35:Bold,55")
                .add("35=D:Fg8,55=AUD&&11=AB:BgBrightYellow")
                .add("35=D:Fg8:Line,55=AUD:BgBrightGreen")
                .endTable()
                .toFormattedText()))

        addOptionHelp(helpByOptions, OptionHelp(
                listOf("F", "output-line-format"),
                "The format of each line.",
                "Thread:${'$'}1 ${'$'}{msgFix}",
                docWriterFactory.createNew().writeLn("The output-line-format is a specification of how each outputted line should be displayed.  The output-line-format param is free text, and can include any of the following tokens:")
                .addTable()
                .startNewRow().addTableHeaderCell("token").addTableHeaderCell("description")
                .startNewRow().addCell("${'$'}{senderToTargetCompIdDirection}").addCell("will display the compIds of the current msg. e.g. ABC->DEF")
                .startNewRow().addCell("${'$'}{msgColor}").addCell("will 'start' formatting text using the pre-defined color for the particular FIX message type.")
                .startNewRow().addCell("${'$'}{msgReset}").addCell("stops formatting text using the pre-defined color for the FIX message.")
                .startNewRow().addCell("${'$'}{msgTypeName}").addCell("displays the FIX msg type name & sub-type, e.g. NewOrderSingle, CancelRequest, Exec.Fill")
                .startNewRow().addCell("${'$'}{msgFix}").addCell("displays the formatted FIX message")
                .startNewRow().addCell("${'$'}{n}").addCell("displays the value of that fix tag.  e.g. for a NewOrderSingle ${'$'}{35} would print 'D'")
                .startNewRow().addCell("${'$'}n").addCell("(note, no braces), will print the captured regex group 'n' from the regex specified in the parameter input-line-format (or property input.line.format).")
                .endTable()
                .toFormattedText()))

        addOptionHelp(helpByOptions, OptionHelp(listOf("G", "line-regexgroup-for-fix"), "Combined with the 'input-line-format' parameter, is used to specify which 'capturing group' of the regex contains the actual fix message.", "2",null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("l", "launch-browser"), "Will launch a browser containing the output log file.", null, "Will open the output file in the system browser.  Can only be used if the -f or --to-file option has been given."))

        addOptionHelp(helpByOptions, OptionHelp(listOf("m", "include-only-messages-of-type"), "A comma separated list of msg types to display.", "D,8", "e.g. to display only NewOrderSingles and ExecutionReports, use 35,8"))


        addOptionHelp(helpByOptions, OptionHelp(listOf("n", "no-color", "suppress-colors"), "Suppresses any colorization in lines.", "true", "Note, see the 'suppress-bold-tags-and-values' parameter to also suppress usage of bold text effect on formatted lines"))

        addOptionHelp(helpByOptions, OptionHelp(listOf("o", "output-delimiter", "output-delim"), "Defines the delimiter to print between FIX tags in the formatted output.", ";", null))

        addOptionHelp(helpByOptions, OptionHelp(
                listOf("p", "suppress-bold-tags-and-values"),
                "Suppresses the bold formatting of tags and values.",
                "true",
                docWriterFactory.createNew().write("By default tags and values are displayed in bold. E.g.: '[MsgType]").writeBold("35=D").write("[NEWORDERSINGLE]|[ClOrdID]").writeBold("11=ABC").write("[Symbol]").writeBold("55=AUD/USD").write(".  In some shells highlighting and bold text don't work well together resulting in partially highlighted fields and lines.  On such shells the user may wish to suppress bold tags and values, so that highlighting is not broken.").toFormattedText()))

        addOptionHelp(helpByOptions, OptionHelp(
                listOf("R", "input-line-format"),
                "Defines the regex to use, when parsing input lines.", 
                "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[(\\d)\\] (\\d+=.*)",
                docWriterFactory.createNew().write("Although FIX messages appear in some logs as 'pure' FIX, they can also be displayed as part of an application's normal logging, and may have text appearing before/after the FIX message.  Such as timestamps, thread identifiers, etc.  This line can be customized for your application so that fixgrep knows where to find the actual FIX message in each log line.  Use in conjunction with the parameter 'line-regexgroup-for-fix' to tell fixgrep, which regex 'group' to use.  The default value for this is ").writeBold("^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(\\d+=.*${'$'})").write(", which looks for an optional ISO timestamp followed by any characters, followed by any number of tag=value pairs which make up the FIX message.  The regex 'group' that contains the FIX message is number 2, as the second set of capturing brackets (\\d+=.*${'$'}) is where we'd expect to find the fix tag.  This default format should match against most variations of FIX logging formats, however you might want to modify it if there is any additional information that you wish to 'capture' and print back out by modifying the line=format parameter, and specifying ${'$'}n tokens.  Or if you wish to optimize the searching.  For example, if your logs contain just FIX messages (no other text), input-line-format could be defined as '.*' and line-regexgroup-for-fix defined as zero 0.  (In regex group 0 is a 'special' group which returns the entire match.)").toFormattedText()))

        addOptionHelp(helpByOptions, OptionHelp(listOf("s", "sort-by-tags"), "Defines the preferred order of the FIX tags in the formatted output.", "35,11", "Let's face it, some tags are more interesting than others.  This parameter allows you to display the more 'interesting' tags at the front of the outputted message."))

        addOptionHelp(helpByOptions, OptionHelp(listOf("t", "only-include-tags"), "Tags to include in the formatted fix.", "35,55,11", """A comma separated list of tags numbers to include in the output FIX.  Any other fields are discarded."""))

        addOptionHelp(helpByOptions, OptionHelp(listOf("v", "exclude-messages-of-type"), "Comma separated list of msgType codes.  Can be used to hide messages of certain types from being displayed.", "A,O", "e.g. To 'hide' Logon and Heartbeat messages, this parameter could be defined as 'A,0'"))

        addOptionHelp(helpByOptions, OptionHelp(listOf("?", "help"), "Displays help text", "",null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("skip-app-properties-creation"), "Skips the creation of a customizable application.properties file in the ~/.fixgrep directory", "true","By default fixgrep will try an create a folder in the users home directory called '.fixgrep', and inside that folder a an application.properties file that the user can customize.  Set this property/option if you wish to skip this step."))

        addOptionHelp(helpByOptions, OptionHelp(listOf("256-color-demo"), "Displays a table of 256 colors using 8 bit Ansi Escape codes.", null,null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("16-color-demo"), "Displays a list of 16 foreground colors and 16 background colors using 16 color Ansi Escape codes.", null,null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("man"), "Displays man page.", null,null))

        addOptionHelp(helpByOptions, OptionHelp(listOf("html"), "Displays results in HTML format.", "page","If the optional 'page' attribute is specified, then the HTML will be contain proper headers and footers.  It will also reference fixgrep.css  Use the option --gimme-css to get a copy of this css file if you wish to use this default styling.  Or of course feel free to modify/write your own css."))

        addOptionHelp(helpByOptions, OptionHelp(listOf("gimme-css"), "Downloads a copy of the default fixgrep.css file to use with any fixgrep output formatted in HTML.", null,null))

        helpByOptions
    }

    private fun addOptionHelp(helpByOptions: MutableMap<String, OptionHelp>, optionHelp: OptionHelp) {
        for(option in optionHelp.optionVariations.values){
            helpByOptions.put(option, optionHelp)
        }
    }

    inner class OptionHelp(val optionVariations: OptionVariations, val tagline: String, val exampleValue: String?, val description: String?){
        constructor(options: List<String>, tagline: String, exampleValue: String?, description: String?): this(OptionVariations(options), tagline, exampleValue, description)

        val longestOptionVariation:String by lazy {
            optionVariations.longest
        }

        fun toFormattedText(): String {
            val writer = docWriterFactory.createNew()
            writer.startSection(HtmlOnlyTextEffect("option"))
                    .writeLn(optionVariations.toString(), MiscTextEffect.Bold)
                    .writeLn(tagline, HtmlOnlyTextEffect("tagline"))
            if(description != null) writer.writeLn(description, HtmlOnlyTextEffect("description"))
            return writer.writeLn().endSection().toFormattedText()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OptionHelp) return false

            if (optionVariations != other.optionVariations) return false
            if (tagline != other.tagline) return false
            if (description != other.description) return false

            return true
        }

        override fun hashCode(): Int {
            var result = optionVariations.hashCode()
            result = 31 * result + tagline.hashCode()
            result = 31 * result + (description?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "OptionHelp(optionVariations=$optionVariations, tagline='$tagline', description=$description)"
        }
    }

    class OptionVariations (val values: List<String>): List<String> by values{
        override fun toString(): String {
            val sb = StringBuilder()
            values.forEach {
                if(sb.length > 0) sb.append(", ")
                sb.append(if(it.length == 1) "-" else "--")
                sb.append(it)
            }
            return sb.toString()
        }

        val longest: String by lazy {
            values.sortedBy { it.length }.last()
        }

        val longestAsPropertyKey: String by lazy {
            longest.replace('-', '.')
        }
    }

    companion object {
        val fix = "35=D|11=ABC|55=AUD/USD"

        @JvmStatic
        fun main(args: Array<String>) {
            println(OptionsHelp(DocWriterFactory.ConsoleText).toFormattedText())
        }
    }
}