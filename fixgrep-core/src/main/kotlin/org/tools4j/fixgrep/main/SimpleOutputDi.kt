package org.tools4j.fixgrep.main

import java.io.OutputStream
import java.io.PrintStream

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:46
 */
class SimpleOutputDi(override val outputStream: OutputStream): OutputDi {
    override val printStream: PrintStream by lazy {
        PrintStream(outputStream)
    }
}