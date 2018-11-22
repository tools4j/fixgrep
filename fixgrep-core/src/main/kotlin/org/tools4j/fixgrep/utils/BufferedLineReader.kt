package org.tools4j.fixgrep.utils

import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 04:03
 */
class BufferedLineReader(val bufferedReader: BufferedReader): LineReader, Closeable {
    constructor(inputStream: InputStream): this(inputStream.bufferedReader())

    override fun readLine(): String? {
        return bufferedReader.readLine()
    }

    override fun close() {
        bufferedReader.close()
    }
}