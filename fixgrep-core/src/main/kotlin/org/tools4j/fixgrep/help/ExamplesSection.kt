package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.highlights.ExamplesList
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect

/**
 * User: ben
 * Date: 22/05/2018
 * Time: 5:50 PM
 */
class ExamplesSection(val docWriterFactory: DocWriterFactory) {
    fun toFormattedText(): String {
        val line1 = "35=D|11=ABC|55=AUD/USD"
        val line2 = "35=8|150=F|55=AUD/USD"

        val docWriter = docWriterFactory.createNew()
        docWriter
                .writeHeading(1, "EXAMPLES")
                .writeLn("Examples are being applied to the following two lines of fix: ")
                .startSection(HtmlOnlyTextEffect.Console)
                .writeLn(line1)
                .writeLn(line2)
                .endSection()

        val examplesList = ExamplesList(listOf(line1, line2), docWriter)
        
        examplesList.add("--exclude-messages-of-type 8", "Excludes messages of type 8 (ExecutionReport).")
        examplesList.add("-v 8", "Same exclusion using the 'short form' option v")
        examplesList.add("-v D", "xxx")
        examplesList.add("-v D,8", "xxx")
        examplesList.add("--tag-annotations __", "xxx")
        examplesList.add("-a __", "xxx")
        examplesList.add("-a none", "xxx")
        examplesList.add("-a b_", "xxx")
        examplesList.add("-a a_", "xxx")
        examplesList.add("-a outsideAnnotated", "xxx")
        examplesList.add("-a ba", "xxx")
        examplesList.add("-a insideAnnotated", "xxx")
        examplesList.add("-a ab", "xxx")
        examplesList.add("-a _a", "xxx")
        examplesList.add("--include-only-messages-of-type D", "xxx")
        examplesList.add("-m D", "xxx")
        examplesList.add("-m D -v D", "xxx")
        examplesList.add("--output-delimiter :", "xxx")
        examplesList.add("-o :", "xxx")
        examplesList.add("--sort-by-tags 55,35", "xxx")
        examplesList.add("-s 55,35", "xxx")
        examplesList.add("-s 55,11,35", "xxx")
        examplesList.add("--only-include-tags 35", "xxx")
        examplesList.add("-t 35", "xxx")
        examplesList.add("-t 35,55", "xxx")
        examplesList.add("-t 35,11", "xxx")
        examplesList.add("-t 666", "xxx")
        examplesList.add("--exclude-tags 11", "xxx")
        examplesList.add("-e 11", "xxx")
        examplesList.add("-t 11 -e 11", "xxx")
        examplesList.add("--highlights 35", "xxx")
        examplesList.add("-h 35", "xxx")
        examplesList.add("-h 35 -p", "xxx")
        examplesList.add("-h 35=8", "xxx")
        examplesList.add("-h 35,55", "xxx")
        examplesList.add("-h 35:Bg8,55:Bg9:Field", "xxx")
        examplesList.add("-h 35=D:Line,55", "xxx")
        examplesList.add("-F '${'$'}{msgColor}[${'$'}{msgTypeName}]${'$'}{colorReset} ${'$'}{msgFix}'", "xxx")
        examplesList.add("-F '${'$'}{msgColor}[${'$'}{msgTypeName}]${'$'}{colorReset} blah ${'$'}{msgFix}'", "xxx")
        examplesList.add("-h 35 -F '${'$'}{msgColor}[${'$'}{msgTypeName}]${'$'}{colorReset} ${'$'}{msgFix}'", "xxx")
        examplesList.add("-h 35 -n -F '${'$'}{msgColor}[${'$'}{msgTypeName}]${'$'}{colorReset} ${'$'}{msgFix}'", "xxx")
        examplesList.add("--suppress-bold-tags-and-values", "xxx")
        examplesList.add("-p", "xxx")

        return ""
    }

    class Example(var command: String, var desc: String){

    }
}