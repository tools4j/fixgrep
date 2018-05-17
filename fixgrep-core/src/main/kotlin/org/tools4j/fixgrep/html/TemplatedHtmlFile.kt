package org.tools4j.fixgrep.html

import org.tools4j.utils.ExistingFileForInput
import java.awt.Desktop
import java.io.BufferedReader
import java.io.File


/**
 * User: ben
 * Date: 16/05/2018
 * Time: 5:27 PM
 */
class TemplatedHtmlFile(htmlTemplate: File, htmlOutputPath: String): HtmlFile(htmlOutputPath) {
    constructor(htmlTemplate: File): this(htmlTemplate, HtmlFile.createTempFixGrepHtmlFilePath())
    val contentTag = "${'$'}{content}"

    val templateBufferedRunnable: BufferedReader by lazy {
        ExistingFileForInput("fixgrep-simple-template.html").bufferedReader
    }

    var trailingTextOnLineAfterContentTag = ""

    override fun afterOpen() {
        super.afterOpen()
        while(true){
            val line = templateBufferedRunnable.readLine()
            if(line.contains(contentTag)){
                val prefixToTag = line.substringBefore(contentTag)
                trailingTextOnLineAfterContentTag = line.substringAfter(contentTag)
                write(prefixToTag)
            }
            writeLine(line)
    }

    override fun beforeClose() {
        super.beforeClose()
    }
}