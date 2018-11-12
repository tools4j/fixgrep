package org.tools4j.fixgrep.integration

import mu.KotlinLogging
import java.io.File

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class FixGrepCommand(val timeoutMs: Long, val command: String) {
    constructor(command: String): this(5000, command)

    fun go(): BashCommand {
        val workingDir = System.getProperty("user.dir")
        val pathPrefix: String = getPathPrefix()
        val bashCommand = BashCommand(timeoutMs, "cd '$workingDir' && cd ${pathPrefix}/build/dist/files && $command").run()
        return bashCommand
    }

    private fun getPathPrefix(): String {
        val pathPrefix: String
        if (File("./fixgrep-core").exists()) {
            logger.info { "Detected that we are in the root of the project..." }
            pathPrefix = "fixgrep-core"
        } else if (File("../fixgrep-core").exists()) {
            logger.info { "Detected that we are in the fixgrep-core dir of the project..." }
            pathPrefix = ""
        } else {
            throw IllegalStateException("Cannot work out where we are in the project.  Current dir: [" + File("").absolutePath + "]")
        }
        return pathPrefix
    }

    companion object {
        val logger = KotlinLogging.logger {}

        init {
            println("NOTE: the integration tests run against an unzipped version of FixGrep.  If your tests are failing, or, if your tests seem to be testing against old code, then be sure to run the integrationTest gradle task in order to ensure that the FixGrep zip file being used is up-to-date")
        }

        val HELP_CONTENT = """Usage: fixgrep [options] [files ...]
Options:
-a,--tag-annotations                     Defines the format of annotations to use when printing fields.
-A,--align-vertical-columns,--align      Aligns tags, values and annotations when viewing messages in vertical format.
-d,--input-delimiter,--input-delim       Defines the FIX delimiter used in the input fix messages.  Default to control character 1, i.e. \u0001
-e,--exclude-tags                        Tags to exclude from the formatted FIX.
-f,--to-file                             Send output to a file.
-O,--group-by-order                      Group order messages by orderId
-h,--highlights,--highlight              Highlight fields or lines using color or console text effects.
--output-format-horizontal-console       The format of each message when displaying fix on the console in horizontal format.
--output-format-vertical-console         The format of each message when displaying fix in html in horizontal format.
--output-format-horizontal-html          The format of each message when displaying fix on the console in horizontal format.
--output-format-vertical-html            The format of each line when displaying fix on the console in horizontal format.
-g,--line-regexgroup-for-fix             Combined with the 'input-line-format' parameter, is used to specify which 'capturing group' of the regex contains the actual fix message.
--install                                Create a customizable application.properties file in the ~/.fixgrep directory
-l,--launch-browser                      Will launch a browser containing the output log file.
-m,--include-only-messages-of-type       A comma separated list of msg types to display.
-n,--suppress-colors,--no-color          Suppresses any colorization in lines.
-o,--output-delimiter,--output-delim     Defines the delimiter to print between FIX tags in the formatted output.
-q,--suppress-bold-tags-and-values       Suppresses the bold formatting of tags and values.
-r,--input-line-format                   Defines the regex to use, when parsing input lines.
-s,--sort-by-tags                        Defines the preferred order of the FIX tags in the formatted output.
-t,--only-include-tags                   Tags to include in the formatted fix.
-v,--exclude-messages-of-type            Comma separated list of msgType codes.  Can be used to hide messages of certain types from being displayed.
-V,--vertical-format                     View messages in vertical format.  Default is false (horizontal).
-G,--indent-group-repeats                Indent group repeats when viewing messages in vertical format.
-x,--debug                               Run in debug mode.
-?,--help                                Displays help text
--256-color-demo                         Displays a table of 256 colors using 8 bit Ansi Escape codes.
--16-color-demo                          Displays a list of 16 foreground colors and 16 background colors using 16 color Ansi Escape codes.
--man                                    Displays man page.
--html                                   Displays results in HTML format.
--gimme-css                              Downloads a copy of the default fixgrep.css file to use with any fixgrep output formatted in HTML.
--fix-spec-path,--fix-spec               Specifies an alternative fixspec definition to use.  Spec must be in the format used by quickfix.  Default spec is 5.0-SP2.

(type 'fixgrep --man' for more detailed help)
"""
    }
}