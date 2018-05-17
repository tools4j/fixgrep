package org.tools4j.fixgrep.html

import java.io.File
import java.awt.Desktop
import java.io.BufferedWriter


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

    fun open(){
        afterOpen()
    }

    fun close(){
        beforeClose()
        writer.flush()
    }

    open fun afterOpen(){}

    open fun beforeClose(){}

    fun writeLine(line: String){
        writer.write(line)
        writer.write("\n")
    }

    fun write(str: String){
        writer.write(str)
    }

    fun writeDiv(classes: String, content: String){
        writeLine("<div class='$classes'>\n" )
        writeLine(content)
        writeLine("\n</div>\n")
    }

    fun writeSpan(classes: String, content: String){
        writeLine("<span class='$classes'>\n" )
        writeLine(content)
        writeLine("\n</span>\n")
    }

    fun writeHeading(level: Int, content: String){
        writeLine("<h$level>\n" )
        writeLine(content)
        writeLine("\n</h$level>\n")
    }

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