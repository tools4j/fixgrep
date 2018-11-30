package org.tools4j.fixgrep.main

import java.io.OutputStream
import java.io.PrintStream

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:51
 */
class Output(val outputStream: OutputStream) {
    val printStream: PrintStream by lazy {
        PrintStream(outputStream)
    }
}