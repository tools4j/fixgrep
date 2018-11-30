package org.tools4j.fixgrep.utils

import mu.KLogging
import org.tools4j.fixgrep.main.FixGrep.Companion.logger
import java.io.Closeable
import java.io.InputStream

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 04:03
 */
class CompositeLineReader(private val lineReaders: Collection<LineReader>): LineReader {
    constructor(is1: InputStream, is2: InputStream): this(listOf(BufferedLineReader(is1), BufferedLineReader(is2)))
    constructor(lines1: String, lines2: String): this(listOf(StringLineReader(lines1), StringLineReader(lines2)))
    companion object: KLogging()

    private val lineReadersIterator = lineReaders.iterator()
    private var currentLineReader: LineReader? = if(lineReadersIterator.hasNext()) lineReadersIterator.next() else null

    override fun readLine(): String? {
        if(currentLineReader == null) return null
        val line = currentLineReader!!.readLine();
        if(line != null){
            return line
        } else if(lineReadersIterator.hasNext()){
            logger.debug { "Detected that the currentLineReader has nothing left to read, moving to next lineReader" }
            currentLineReader = lineReadersIterator.next()
            return readLine()
        } else {
            logger.debug { "Detected that the currentLineReader has nothing left to read, and that there are no more lineReaders.  Exiting." }
            currentLineReader = null
            return null
        }
    }

    override fun close(){
        for (closeable in lineReaders) {
            closeable.close()
        }
    }
}