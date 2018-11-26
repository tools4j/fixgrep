package org.tools4j.fixgrep.help

import mu.KotlinLogging
import org.tools4j.fixgrep.config.Option
import org.tools4j.fixgrep.help.HelpGenerator.Companion.fix
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
        optionsHelp.forEach{sb.append(it.toFormattedText())}
        return sb.toString()
    }

    val optionsThatDoNotNeedHelpItems: List<Option> by lazy {
        ArrayList<Option>()
    }

    val optionsHelp: List<OptionHelp> by lazy {
        val optionsHelp = ArrayList<OptionHelp>()

        optionsHelp.add(OptionHelp(
                Option.tag_annotations,
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

        optionsHelp.add(OptionHelp(Option.align_vertical_columns, "Aligns tags, values and annotations when viewing messages in vertical format.", null, null))

        optionsHelp.add(OptionHelp(Option.input_delimiter, "Defines the FIX delimiter used in the input fix messages.  Default to control character 1, i.e. \\u0001", ":", null))

        optionsHelp.add(OptionHelp(Option.output_delimiter, "Defines the delimiter to print between FIX tags in the formatted output.", ";", null))

        optionsHelp.add(OptionHelp(Option.exclude_tags, "Tags to exclude from the formatted FIX.", "22,33", """A comma separated list of tags which should not be displayed in the output 'formatted' fix.
e.g. '--exclude-tags 22,33' would hide tags 22 and 33 from being displayed.  Can be useful for hiding some of
the less 'interesting' fix fields, such as BeginString, BodyLength or Checksum.  Yawn!"""))

        optionsHelp.add(OptionHelp(Option.to_file, "Send output to a file with a generated filename.  Filename is printed to std out after being written..", null, """A random file-htmlClass will be generated, with the prefix 'fixgrep-'.  And an extension of '.log' if in normal console mode, or an extension of '.html' if output is in html."""))

        optionsHelp.add(OptionHelp(Option.to_given_file, "Send output to a given file.", "output.txt", """Output is printed to the specified file."""))

        optionsHelp.add(OptionHelp(Option.group_by_orders_with_id, "Group order messages by orderId and/or clientOrderId.  Only orders which have an id containing the given text will be matched.", null,
                "Using this option changes the 'mode' of fixgrep to discard any non-order messages, and to group the messages by order. fixgrep will attempt to keep track of 'order chains' when amends and cancels change the clOrdId, even if the orderId is not present on every message.  Using this option can increase the memory used by fixgrep, as fixgrep will need to cache all order messages before printing them out.  Even if order messages do not contain the given id, fixgrep still needs to cache them in case it discovers and amend in the order chain which DOES contain the given id.  This should not affect other processes on the box, but it might mean that fixgrep will stop with an OutOfMemoryException if it uses all of it's allocated heap."))

        optionsHelp.add(OptionHelp(Option.group_by_order, "Group order messages by orderId and/or clientOrderId.", null,
                "Using this option changes the 'mode' of fixgrep to discard any non-order messages, and to group the messages by order. fixgrep will attempt to keep track of 'order chains' when amends and cancels change the clOrdId, even if the orderId is not present on every message.  Using this option can increase the memory used by fixgrep, as fixgrep will need to cache all order messages before printing them out.  This should not affect other processes on the box, but it might mean that fixgrep will stop with an OutOfMemoryException if it uses all of it's allocated heap."))

        optionsHelp.add(OptionHelp(
                Option.highlights,
                "Highlight fields or lines using color or console text effects.", 
                "35,55",
                docWriterFactory.createNew()
                .write("Format: ").writeBold("criteria[:texteffect1][:texteffect2][:scope]")
                .writeLn(" (criteria is mandatory, texteffect and scope are optional.)")
                .writeHeading(3, "criteria")
                .writeLn(" must start with a tag number, and can optionally have an equals or tilda followed by a string to match the tagsInId value against.  An equals sign '=' performs an exact match.  If just the tag number is specified, the tag and associated value will be highlighted. Multiple tag (and optional equals value) pairs can be chained together into an 'and' statement using the && operator.  'Or' statements are not supported as of yet, as you can just specify another highlight.")
                .writeHeading(3, "texteffect1 & texteffect2")
                .writeLn(" are optional. It uses ansi escape codes to print display attributes which affect how the text is displayed.  If this is not specified, fixgrep will loop through a predefined set of foreground colors. If specified, it can be of four different formats. 16 color text effects, 256 color text effects, Bold, or a raw escape code.  Whether these escape codes will work will depend on your particular shell environment.")
                .writeHeading(3, "scope")
                .writeLn(" is optional, and can be either 'Field' or 'Line', which controls whether just the field is highlighted, or the whole line.  If not specified, just the Field is highlighted.'")
                .writeHeading(3, "Specifying texteffects")
                .startList()
                .startListItem().writeBold("Defaults - ").write("If no texteffect is specified, then default colors are used.  Fixgrep will loop through a predefined set of foreground colors.  Those being: ").writeListOfDefaultColors().endListItem()
                .startListItem().writeBold("16-color text effects - ").write("comprise of the ansi 8 colors, and 8 'bright' colors, used as either a foreground text color, or a background color.  You can specify these by using the format [Fg|Bg][Color]. e.g. foreground colors: ")
                .writeListOfAnsi16ForegroundColors()
                .write(" and background colors: ")
                .writeListOfAnsi16BackgroundColors().endListItem()
                .startListItem().writeBold("256-color text effects - ").write("You can see a 256 color palette by running 'fixgrep --256-color-demo' (This will also test if your console supports 256 colors.)  Of if you're viewing the online man page, you should be able to see the palette listed under the --256-color-demo help section.  To use colors from the 256 color palette, specify these using the format [Fg|Bg][num]. e.g. ")
                .write("Fg5", Ansi256Color(5, AnsiForegroundBackground.FOREGROUND)).write(", ")
                .write("Fg23", Ansi256Color(23, AnsiForegroundBackground.FOREGROUND)).write(", ")
                .write("Bg107", Ansi256Color(107, AnsiForegroundBackground.BACKGROUND)).write(", ")
                .write("Bg58", Ansi256Color(58, AnsiForegroundBackground.BACKGROUND)).endListItem()
                .startListItem().writeBold("Raw Escape Codes - ").write("Allows you to specify whatever codes you want. e.g. RGB colors in the format").write("\\u001B[38;2;r;g;bm", MiscTextEffect.Console).write(" (where r,g,b are the numeric RGB values.)  As long as the texteffect string starts with the escape code followed by a left square bracket, fixgrep will assume you are using a raw escape code.").endListItem()
                .endList()
                .writeHeading(3, "Example highlights:")
                .writeFormatExamplesTable(fix)
                .add("35")
                .add("35:Bold")
                .add("35:Bold:Msg")
                .add("11")
                .add("11:FgWhite:BgBlue")
                .add("11:FgWhite:BgBlue:Field")
                .add("11:FgWhite:BgBlue:Msg")
                .add("11:Field")
                .add("11:Msg")
                .add("35=D")
                .add("35=D:BgBlue:FgWhite")
                .add("35=D:Fg176")
                .add("35=D:Fg177:Msg")
                .add("35=D&&55=AUD")
                .add("35=D&&55=AUD:FgRed")
                .add("35=D&&55=AUD:FgRed:Field")
                .add("35=D&&55=AUD:FgRed:Msg")
                .add("35=D&&55=AUD&&11=AB:FgPurple")
                .endTable()
                .writeHeading(3,"Some examples specifying multiple highlights (separated by commas):")
                .writeFormatExamplesTable(fix)
                .add("35,55")
                .add("35=D,55=AUD")
                .add("35=D,55=XYZ")
                .add("35,55,11")
                .add("35:Bold,55")
                .add("35=D:Fg176,55=AUD&&11=AB:FgBlue:BgBrightYellow")
                .add("35=D:Fg176:Msg,55=AUD:BgBrightGreen")
                .endTable()
                .toFormattedText()))

        optionsHelp.add(OptionHelp(
                Option.output_format_horizontal_console,
                "The format of each message when displaying fix on the console in horizontal format.",
                "Thread:${'$'}1 ${'$'}{msgFix}",
                docWriterFactory.createNew().writeLn("See the section on 'Output Formatting' for more information.")
                .toFormattedText()))

        optionsHelp.add(OptionHelp(
                Option.output_format_vertical_console,
                "The format of each message when displaying fix in html in horizontal format.",
                "==========\\n${'$'}{msgColor}${'$'}{msgTypeName}${'$'}{colorReset}\\n==========\\n${'$'}{msgFix}",
                docWriterFactory.createNew().writeLn("See the section on 'Output Formatting' for more information.")
                        .toFormattedText()))

        optionsHelp.add(OptionHelp(
                Option.output_format_horizontal_html,
                "The format of each message when displaying fix on the console in horizontal format.",
                "<b>Thread:</b>${'$'}1 ${'$'}{msgFix}",
                docWriterFactory.createNew().writeLn("See the section on 'Output Formatting' for more information.")
                        .toFormattedText()))

        optionsHelp.add(OptionHelp(
                Option.output_format_vertical_html,
                "The format of each line when displaying fix on the console in horizontal format.",
                "<div class='msg-header'>\\n==========</br>\\n${'$'}{msgColor}${'$'}{msgTypeName}${'$'}{colorReset}<br/>\\n==========\\n</div>\\n${'$'}{msgFix}\\n<br/>",
                docWriterFactory.createNew().writeLn("See the section on 'Output Formatting' for more information.")
                        .toFormattedText()))

        optionsHelp.add(OptionHelp(Option.line_regexgroup_for_fix, "Combined with the 'input-line-format' parameter, is used to specify which 'capturing group' of the regex contains the actual fix message.", "2",null))

        optionsHelp.add(OptionHelp(Option.install, "Create a customizable application.properties file in the ~/.fixgrep directory", null,"Fixgrep will try to create a folder in the users home directory called '.fixgrep', and inside that folder fixgrep will try to create an application.properties file that the user can customize."))

        optionsHelp.add(OptionHelp(Option.launch_browser, "Will launch a browser containing the output log file.", null, "Will open the output file in the system browser.  Can only be used if the -f or --to-file option has been given."))

        optionsHelp.add(OptionHelp(Option.include_only_messages_of_type, "A comma separated list of msg types to display.", "D,8", "e.g. to display only NewOrderSingles and ExecutionReports, use 35,8"))

        optionsHelp.add(OptionHelp(Option.suppress_colors, "Suppresses any colorization in lines.", "true", "Note, see the 'suppress-bold-tags-and-values' parameter to also suppress usage of bold text effect on formatted lines"))

        optionsHelp.add(OptionHelp(
                Option.suppress_bold_tags_and_values,
                "Suppresses the bold formatting of tags and values.",
                "true",
                docWriterFactory.createNew().write("By default tags and values are displayed in bold. E.g.: '[MsgType]").writeBold("35=D").write("[NEWORDERSINGLE]|[ClOrdID]").writeBold("11=ABC").write("[Symbol]").writeBold("55=AUD/USD").write(".  In some shells highlighting and bold text don't work well together resulting in partially highlighted fields and lines.  On such shells the user may wish to suppress bold tags and values, so that highlighting is not broken.").toFormattedText()))

        optionsHelp.add(OptionHelp(
                Option.input_line_format,
                "Defines the regex to use, when parsing input lines.", 
                "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[(\\d)\\] (\\d+=.*)",
                docWriterFactory.createNew().write("Although FIX messages appear in some logs as 'pure' FIX, they can also be displayed as part of an application's normal logging, and may have text appearing before/after the FIX message.  Such as timestamps, thread identifiers, etc.  This line can be customized for your application so that fixgrep knows where to find the actual FIX message in each log line.  Use in conjunction with the parameter 'line-regexgroup-for-fix' to tell fixgrep, which regex 'group' to use.  The default value for this is ").writeBold("^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(\\d+=.*${'$'})")
                        .writeLn(", which looks for an optional ISO timestamp followed by any characters, followed by any number of tag=value pairs which make up the FIX message.  The regex 'group' that contains the FIX message is number 2, as the second set of capturing brackets (\\d+=.*${'$'}) is where we'd expect to find the fix tag.  This default format should match against most variations of FIX logging formats, however you might want to modify it if there is any additional information that you wish to 'capture' and print back out by modifying the line=format parameter, and specifying ${'$'}n tokens.  Or if you wish to optimize the searching.  For example, if your logs contain just FIX messages (no other text), input-line-format could be defined as '.*' and line-regexgroup-for-fix defined as zero 0.  (In regex group 0 is a 'special' group which returns the entire match.)")
                        .writeBold("NOTE: Running this regex is the single biggest user of CPU in fixgrep ").write("because the regex is run against every line in the file/pipe being processed.  So it pays to experiment with different regexes to find one that is fastest.  The regex does not need to match the _whole_ line, just the part of the line that indicates it's a line of FIX, and a regex group (brackets) which captures the FIX message.")
                        .toFormattedText()))

        optionsHelp.add(OptionHelp(Option.sort_by_tags, "Defines the preferred order of the FIX tags in the formatted output.", "35,11", "Let's face it, some tags are more interesting than others.  This parameter allows you to display the more 'interesting' tags at the front of the outputted message."))

        optionsHelp.add(OptionHelp(Option.only_include_tags, "Tags to include in the formatted fix.", "35,55,11", """A comma separated list of tags numbers to include in the output FIX.  Any other fields are discarded."""))

        optionsHelp.add(OptionHelp(Option.exclude_messages_of_type, "Comma separated list of msgType codes.  Can be used to hide messages of certain types from being displayed.", "A,O", "e.g. To 'hide' Logon and Heartbeat messages, this parameter could be defined as 'A,0'"))

        optionsHelp.add(OptionHelp(Option.vertical_format, "View messages in vertical format.  Default is false (horizontal).", null, null))

        optionsHelp.add(OptionHelp(Option.suppress_indent_group_repeats, "Suppress indenting of group repeats in vertical format.", null,
                """Often when viewing messages which have a lot of repeating groups e.g. prices, it is useful to see the repeating groups indented.  By default,
                    |groups will be indented in vertical formatting. Use this option to suppress this behaviour.  Has no effect when viewing messages in the default horizontal format.
                    |Note, indentation will also be suppressed if the user is attempting to sort tags.""".trimMargin()))

        optionsHelp.add(OptionHelp(Option.debug, "Run in debug mode.", null, null))

        optionsHelp.add(OptionHelp(Option.help, "Displays help text", "",null))

        optionsHelp.add(OptionHelp(Option.color_demo_256, "Displays a table of 256 colors using 8 bit Ansi Escape codes.", null,
                "Most modern consoles support 256 colors.  To use any of these colors in your highlights, prefix the msgType with 'Fg' or 'Bg' depending on whether you wish to highlight the foreground or background.'" +
                            Color256HtmlDemo(docWriterFactory).toFormattedText()))

        optionsHelp.add(OptionHelp(Option.color_demo_16, "Displays a list of 16 foreground colors and 16 background colors using 16 color Ansi Escape codes.", null,
                "Most consoles support 16 colors.  To use any of these colors in your highlights, prefix the htmlClass of the colors below with 'Fg' or 'Bg' depending on whether you wish to highlight the foreground or background.  E.g. FgWhite, BgRed.  To find out whether your console supports these colors, run this demo by specifying this option." +
                            Color16HtmlDemo(docWriterFactory).toFormattedText()))

        optionsHelp.add(OptionHelp(Option.man, "Displays man page.", null,"Running with this command will print out the man page.  You can also use 'fixgrep man' (no dashes) which will run man and pipe it into less -R which preserves ansi colors.  Or 'fixgrep man online' which will launch the gixgrep online help into your default browser."))

        optionsHelp.add(OptionHelp(Option.html, "Displays results in HTML format.", null,"Displays as raw html.  No css, header or footers will be output."))

        optionsHelp.add(OptionHelp(Option.html_page, "Displays results in a fully formed HTML page.", null, "Outputs html ready to be displayed in a browser.  Will include headers and footers. " +
                "Will also include inline css to format the fix appropriately.  You can also used the -l argument if running on a operating system with a browser, to launch the page into your default browser."))

        optionsHelp.add(OptionHelp(Option.gimme_css, "Downloads a copy of the default fixgrep.css file to use with any fixgrep output formatted in HTML.", null,null))

        optionsHelp.add(OptionHelp(Option.fix_spec_path, "Specifies an alternative fixspec definition to use.  Spec must be in the format used by quickfix.  Default spec is 5.0-SP2.", "FIX40_modified.xml","FixGrep first looks for the file relative to the current working directory.  If it is not found there, then FixGrep will look on the classpath.  Examples:\nmy-fix-spec.xml\npath/to/my-fix-spec.xml\n/package/path/to/my-fix-spec.xml"))

        //Verify we have all the options
        val optionsThatHaveHelpDefined = optionsHelp.map { it.option }
        val optionsThatWeDontHaveHelpYetDefined = ArrayList<Option>()
        for(option in Option.optionsThatCanBePassedOnCommandLine){
            if(!optionsThatHaveHelpDefined.contains(option) && !optionsThatDoNotNeedHelpItems.contains(option)){
                optionsThatWeDontHaveHelpYetDefined.add(option)
            }
        }
        if(!optionsThatWeDontHaveHelpYetDefined.isEmpty()){
            throw IllegalStateException("We still need to define help items for options $optionsThatWeDontHaveHelpYetDefined")
        }

        optionsHelp
    }

    inner class OptionHelp(val option: Option, val tagline: String, val exampleValue: String?, val description: String?){
        fun toFormattedText(): String {
            val writer = docWriterFactory.createNew()
            writer.startSection(HtmlOnlyTextEffect("option"))
                    .writeHeading(2, option.optionVariationsWithDashPrefixesAsCommaDelimitedString)
                    .writeLn(tagline, HtmlOnlyTextEffect("tagline"))
            if(description != null) writer.write(description, HtmlOnlyTextEffect("description"))
            return writer.endSection().toFormattedText()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OptionHelp) return false

            if (option != other.option) return false
            if (tagline != other.tagline) return false
            if (description != other.description) return false

            return true
        }

        override fun hashCode(): Int {
            var result = option.hashCode()
            result = 31 * result + tagline.hashCode()
            result = 31 * result + (description?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "OptionHelp(optionVariations=$option, tagline='$tagline', description=$description)"
        }
    }

    companion object {
        val logger = KotlinLogging.logger {}
        @JvmStatic
        fun main(args: Array<String>) {
            logger.info(OptionsHelp(DocWriterFactory.Html).toFormattedText())
        }
    }
}