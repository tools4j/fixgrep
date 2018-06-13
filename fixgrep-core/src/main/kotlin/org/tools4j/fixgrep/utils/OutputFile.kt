package org.tools4j.fixgrep.utils

import java.awt.Desktop
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStream


/**
 * User: ben
 * Date: 16/05/2018
 * Time: 5:27 PM
 */
open class OutputFile(val path: String, val synthesizedFilename: Boolean = false) {
    constructor(extension: Extension) : this(createTempFile(extension), true)

    val fileObj: File by lazy {
        val file = File(path)
        if(file.exists() && !file.readText().isEmpty()){
            file.delete()
            file.createNewFile()
        }
        file
    }

    val writer: BufferedWriter by lazy {
        fileObj.bufferedWriter()
    }

    val outputStream: OutputStream by lazy {
        fileObj.outputStream()
    }

    fun launchInBrowser(){
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(fileObj.toURI())
        } else {
            val runtime = Runtime.getRuntime()
            runtime.exec("/usr/bin/firefox -new-window " + fileObj.absolutePath)
        }
    }

    fun finish() {
        if(synthesizedFilename){
            println("Output written to: " + fileObj.absolutePath)
        }
    }

    enum class Extension{
        html,
        log
    }

    companion object {
        fun createTempFile(extension: Extension): String {
            return createTempFile("fixgrep-", ".$extension", File(System.getProperty("user.dir"))).absolutePath
        }
    }
}