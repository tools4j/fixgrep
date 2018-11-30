package org.tools4j.fixgrep.utils

import java.io.BufferedReader
import java.io.InputStream

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 04:03
 */
class StringLineReader(val lines: String): LineReader {
    val linesIterator = lines.split(Regex("\n(\r)?")).iterator()

    override fun readLine(): String? = if(linesIterator.hasNext()) linesIterator.next() else null
    override fun close() {}
}