package org.tools4j.fixgrep.html

import org.tools4j.fixgrep.help.HtmlFile
import org.tools4j.utils.ExistingFileForInput
import java.io.BufferedReader
import java.io.InputStream


/**
 * User: ben
 * Date: 16/05/2018
 * Time: 5:27 PM
 */
class TemplatedHtmlFile(htmlTemplate: InputStream, htmlOutputPath: String): HtmlFile(htmlOutputPath) {
    constructor(htmlTemplate: InputStream): this(htmlTemplate, HtmlFile.createTempFixGrepHtmlFilePath())
    constructor(): this(ExistingFileForInput("fixgrep-simple-template.html").inputStream, HtmlFile.createTempFixGrepHtmlFilePath())
    val contentTag = "${'$'}{content}"

    val templateBufferedRunnable: BufferedReader by lazy {
        htmlTemplate.bufferedReader()
    }

    var trailingTextOnLineAfterContentTag = ""

    override fun afterOpen() {
        super.afterOpen()
        while(true) {
            val line = templateBufferedRunnable.readLine()
            if (line.contains(contentTag)) {
                val prefixToTag = line.substringBefore(contentTag)
                trailingTextOnLineAfterContentTag = line.substringAfter(contentTag)
                write(prefixToTag)
                break;
            }
            writeLn(line)
        }
    }

    override fun beforeClose() {
        super.beforeClose()
        writeLn(trailingTextOnLineAfterContentTag)
        while(true) {
            val line = templateBufferedRunnable.readLine()
            if(line == null) break;
            writeLn(line)
        }
    }
}