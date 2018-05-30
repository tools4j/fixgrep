package org.tools4j.fixgrep.help

import java.awt.Desktop
import java.io.BufferedWriter
import java.io.File


/**
 * User: ben
 * Date: 16/05/2018
 * Time: 5:27 PM
 */
open class HtmlFile(val path: String) {
    constructor(): this(createTempFixGrepHtmlFilePath())

    val htmlFile: File by lazy {
        val file = File(path)
        if(file.exists() && !file.readText().isEmpty()){
            file.delete()
            file.createNewFile()
        }
        file
    }

    val writer: BufferedWriter by lazy {
        htmlFile.bufferedWriter()
    }

    fun write(content: String): HtmlFile {
        writer.write(content)
        return this
    }

    fun writeLn(content: String): HtmlFile {
        writer.write(content)
        writer.write("<br/>\n")
        return this
    }

    fun open(){
        afterOpen()
    }

    fun close(){
        beforeClose()
        writer.flush()
    }

    open fun afterOpen(){}

    open fun beforeClose(){}


    fun launchInBrowser(){
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(htmlFile.toURI())
        } else {
            val runtime = Runtime.getRuntime()
            runtime.exec("/usr/bin/firefox -new-window " + htmlFile.absolutePath)
        }
    }

    companion object {
        fun createTempFixGrepHtmlFilePath(): String {
            return createTempFile("fixgrep-", ".html", File(System.getProperty("user.dir"))).absolutePath
        }
    }
}