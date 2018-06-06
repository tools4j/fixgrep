package org.tools4j.fix

import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

/**
 * User: ben
 * Date: 29/6/17
 * Time: 5:45 PM
 */
class ClasspathResource(private val path: String) : FileResource {

    override fun asInputStream(): InputStream {
        try {
            return this.javaClass.getResourceAsStream(path)
        } catch (e: Exception) {
            throw RuntimeException("Could not load file at path [" + path + "]")
        }
    }

    fun asBufferedReader(): BufferedReader {
        return BufferedReader(asReader());
    }

    fun asReader(): Reader {
        return InputStreamReader(asInputStream())
    }
}
