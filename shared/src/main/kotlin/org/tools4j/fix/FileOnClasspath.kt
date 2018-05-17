package org.tools4j.fix

import java.io.File
import java.io.InputStream
import java.nio.file.Files

/**
 * User: ben
 * Date: 29/6/17
 * Time: 5:45 PM
 */
class FileOnClasspath(private val path: String) : FileResource {

    override fun asInputStream(): InputStream {
        val cleanedPath = if(path.startsWith("/")) path else "/" + path
        return this.javaClass.getResourceAsStream(cleanedPath)
    }

    fun writeTo(outputFile: File) {
        Files.copy(asInputStream(), outputFile.toPath())
    }
}
