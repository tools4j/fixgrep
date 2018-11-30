package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.utils.BufferedLineReader
import org.tools4j.fixgrep.utils.LineReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*
import kotlin.collections.ArrayList

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:46
 */
class SimpleInputDi(override val lineReader: LineReader): InputDi {
    constructor(inputStream: InputStream): this(BufferedLineReader(inputStream))
    constructor(bufferedReader: BufferedReader): this(BufferedLineReader(bufferedReader))
}
