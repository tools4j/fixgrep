package org.tools4j.fixgrep.help

import org.tools4j.extensions.containsAny
import org.tools4j.fixgrep.OptionParserFactory
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.properties.ConfigAndArguments

/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class ManGenerator(val docWriterFactory: DocWriterFactory, val configAndArguments: ConfigAndArguments, val debug: Boolean = false) {
    val man: String by lazy {
        val sb = StringBuilder()
        sb.append(whatIsFixgrep())
        sb.append(howToGet())
        sb.append(howToGetHelp())
        sb.append(howToInstall())
        sb.append(howToRun())
        sb.append(howToConfigure())
        sb.append(OptionsHelp(docWriterFactory).toFormattedText())
        sb.append(ExamplesSection(docWriterFactory).toFormattedText())

        sb.append(licencing())
        val toString = sb.toString()
        if(debug) toString.replace("\u001b", "\\u001b")
        else toString
    }

    private fun whatIsFixgrep(): String {
        val lines = listOf(
                "8=FIX.5.2^A9=232^A35=D^A11=C28^A55=AUD/USD^A54=2^A38=1464820^A44=100.026",
                "8=FIX.5.2^A9=54^A35=8^A11=C28^A150=0^A151=1464820^A14=0^A44=100.02",
                "8=FIX.5.2^A9=67^A35=G^A9=232^A11=C32^A38=1465320^A40=2^A44=100.12",
                "8=FIX.5.2^A9=23^A35=8^A11=C32^A150=5^A151=1465320^A14=0^A44=100.12",
                "8=FIX.5.2^A9=56^A35=8^A11=C32^A150=2^A151=1072490^A14=392830^A44=100.00")

        val writer = docWriterFactory.createNew()
        val formattedExample = SingleExample(lines, listOf("-e", "8,9,35"), docWriterFactory).toFormattedString()
        writer
                .writeHeading(1, "What is fixgrep")
                .writeLn("fixgrep is a command line utility for searching through, and making FIX protocol messages more readable.")
                .writeLn("fixgrep can turn this:")
                .startSection(MiscTextEffect.Console)
        lines.forEach { writer.writeLn(it) }
        writer.endSection()
                .writeLn("into this:")
                .write(formattedExample)
                .toFormattedText();

        writer.writeHeading(2, "fixgrep features:")
        writer.startList()
        writer.listItem("Filtering of messages by field criteria")
        writer.listItem("Hide tags")
        writer.listItem("Change the order that tags are displayed")
        writer.listItem("Highlight tags and lines based on criteria")
        writer.listItem("Output to text or html format")
        writer.endList()
        return writer.toFormattedText()
    }

    private fun howToGet(): String {
        val fixgrepDownloadUrl = configAndArguments.config.getAsString("fixgrep.download.url")
        val fixgrepVcsUrl = configAndArguments.config.getAsString("fixgrep.vcs.home.url")

        return docWriterFactory.createNew().writeHeading(1, "How to get")
                .write("Download the latest version from ")
                .writeLink("here", fixgrepDownloadUrl).writeLn()
                .write("GitHub project can be found ")
                .writeLink("here", fixgrepVcsUrl).writeLn()
                .toFormattedText();


    }

    private fun howToGetHelp(): String {
        val fixgrepHelpUrl = configAndArguments.config.getAsString("fixgrep.online.help.url")

        return docWriterFactory.createNew().writeHeading(1, "How to get help")
                .startList()
                .startListItem().write("Online ").writeLink("here", "www.blah.com").endListItem()
                .listItem("Running 'fixgrep man online' (no dashes) will open this page in your default browser.")
                .listItem("Running 'fixgrep man' (no dashes) will display the fixgrep man page.")
                .listItem("Running 'fixgrep --man' (with dashes) will display the fixgrep man page in raw format without scrolling.")
                .listItem("Running 'fixgrep -?' will print a list of options.")
                .endList()
                .toFormattedText();
    }

    private fun howToInstall(): String {
        return docWriterFactory.createNew()
                .writeHeading(1, "How to install")
                .writeLn("You will need to install a version of java (you only need a jre, although a jdk will also work fine.)  Version must be version Java 8 for higher.  (Java jre/jdk 1.8 or higher).")
                .writeLn("Once downloaded fixgrep, just unzip the content into a directory.  Put that directory on your path.")
                .toFormattedText();
    }

    private fun howToRun(): String {
        return docWriterFactory.createNew()
                .writeHeading(1, "How to run")
                .writeLn("There is a bash script file 'fixgrep' which should be your main interface to fixgrep. There are two ways to send fix data into fixgrep.  The first is 'piping'  e.g.")
                .writeLn("grep 11=ABC my-large-file.fix | fixgrep [options]", MiscTextEffect.Console)
                .writeLn("The second is specifying a file argument at the end of the fixgrep command.  e.g.")
                .writeLn("fixgrep [options] my-file.fix", MiscTextEffect.Console)
                .writeLn("Note, fixgrep does not curently support wildcards to specify multiple files.  That feature is on it's way.")
                .toFormattedText();
    }

    private fun howToConfigure(): String {
        val options = OptionParserFactory().optionParser.recognizedOptions()
        val writer = docWriterFactory.createNew()
        writer.writeHeading(1, "Ways to configure fixgrep")
                .writeHeading(2, "Fixgrep can be configured via three methods.")
        writer.startList()
                .startListItem().writeBold("Command options.").write(" These are specified as POSIX style options after the fixgrep command.  e.g. fixgrep --highlights 35 will highlight any MsgType tag/values.  It is best to use command line options if your configuration is a once off, or is specific to the context you are running in.").endListItem()
                .startListItem().writeBold("application.properties.").write("If you wish to configure fixgrep to behave in a certain way every time you run it, then it is better to add your configuration to ~/.fixgrep/application.properties. If using the provided .sh file, when running fixgrep for the first time, it should have created a ~/.fixgrep folder, and will add that to your path when running fixgrep.  If fixgrep fails to create a ~/.fixgrep folder, then as a fallback it will create a properties file in the same directory as the .sh script file.").endListItem()
                .endList()
                .writeHeading(2, "Differences between command-line options, and properties configuration")
                .writeLn("Command-line options (usually) provide at least two different options which configure the same parameter.  Usually a short single character option, and a more descriptive option.  e.g. -o, --output-delimiter both configure the output delimiter.  However when configuring via the properties file only one option is available to use.  That is the longest command-line option, with dashes replaced with dots.  e.g. to configure the output delimiter in the properties file, you would need to use output.delimiter option.  e.g. output.delimiter=|  Below is a table listing the command line options, and on the right the equivalent properties to use in properties file configuration.")
        val tableBuilder = writer.addTable()
        tableBuilder.startNewTable(HtmlOnlyTextEffect("options-to-properties-table"))
        tableBuilder.startNewRow().addTableHeaderCell("option variations").addTableHeaderCell("equivalient properties key")
        for (desc in options.values.distinct().sortedBy { it.options().first() }) {
            if (OptionParserFactory.optionsThatShouldNotHaveEquivalentProperties.containsAny(desc.options())) continue
            val optionVariations = OptionsHelp.OptionVariations(desc.options())
            tableBuilder.startNewRow().addCell(optionVariations.toString()).addCell(optionVariations.longestAsPropertyKey)
        }
        tableBuilder.endTable()
        return writer.toFormattedText()
    }

    private fun licencing(): String {
        return docWriterFactory.createNew()
                .writeHeading(1, "Licensing")
                .writeLn("Fixgrep is released under MIT license.")
                .writeLn("It is free to use and/or modify in both commercial and non-comercial environments.")
                .toFormattedText();
    }
}