package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.config.Option
import org.tools4j.fixgrep.utils.OutputFile
import java.io.OutputStream
import java.io.PrintStream

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:46
 */
interface OutputDi {
    val outputStream: OutputStream
    val printStream: PrintStream

    fun flushAndClose(){
        printStream.flush()
        printStream.close()
        outputStream.flush()
        outputStream.close()
    }
}