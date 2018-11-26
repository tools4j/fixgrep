package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import org.tools4j.fixgrep.texteffect.MiscTextEffect
import org.tools4j.fixgrep.utils.WrappedFixGrep
import org.tools4j.utils.ArgsAsString

/**
 * User: ben
 * Date: 24/04/2018
 * Time: 5:25 PM
 */
class SingleExample (val fix: String, val args: List<String>, val docWriterFactory: DocWriterFactory) {
    constructor(fixLines: List<String>, args: String, docWriterFactory: DocWriterFactory): this(fixLines.joinToString("\n"), args, docWriterFactory)
    constructor(fixLines: List<String>, args: List<String>, docWriterFactory: DocWriterFactory): this(fixLines.joinToString("\n"), args, docWriterFactory)
    constructor(fix: String, args: String, docWriterFactory: DocWriterFactory): this(fix, ArgsAsString(args).toArgs(), docWriterFactory)

    fun toFormattedString(): String {
        val docWriter = docWriterFactory.createNew()
        docWriter.startSection(HtmlOnlyTextEffect("example-list"))
        docWriter.startSection(MiscTextEffect.Console)
        val mutatedArgs = ArrayList(args)
        if( docWriter.isHtml() ) mutatedArgs.add("--html")
        val output = WrappedFixGrep(mutatedArgs).go(fix)
        docWriter.write(output)
        docWriter.endSection()
        docWriter.endSection()
        return docWriter.toFormattedText()
    }
}