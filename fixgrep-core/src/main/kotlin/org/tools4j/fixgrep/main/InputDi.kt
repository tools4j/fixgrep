package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.utils.LineReader

/**
 * Ideally, the InputDi, would just use a typical InputStream. There was a problem with that.
 * When I needed to join multiple files as an input, there is a SequenceInputStream class
 * which can join multiple InputStreams together.  HOWEVER, if the end of an InputStream does
 * not contain a EOL character(s), then when reading lines, the last line of an InputStream,
 * followed by the first line of the next InputStream would be joined together.
 *
 * The compositeLineReader takes care of this by wrapping two BufferedReaders.
 */
interface InputDi {
    val lineReader: LineReader
}