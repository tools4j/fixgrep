package org.tools4j.fixgrep

import java.io.File
import java.io.InputStream
import java.io.OutputStream


/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrepMain(val inputStream: InputStream?, val outputStream: OutputStream, val args: Array<String>){
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fixGrepMain = FixGrepMain(System.`in`, System.out, args)
            fixGrepMain.go()
        }
    }

    private fun go() {
        val configAndArguments = ConfigBuilder(args).configAndArguments
        val fixGrepOutputStream: OutputStream
        if(configAndArguments.config.hasProperty("to.file")){
            val toFile = configAndArguments.config.getAsString("to.file")
            val outputFile = File(toFile)
            fixGrepOutputStream = outputFile.outputStream()
        } else {
            fixGrepOutputStream = outputStream
        }
        FixGrep(inputStream, fixGrepOutputStream, configAndArguments).go()
    }
}