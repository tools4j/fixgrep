package org.tools4j.utils

import java.io.BufferedReader
import java.io.File
import java.io.InputStream

/**
 * User: ben
 * Date: 17/05/2018
 * Time: 6:55 AM
 */
class ExistingFileForInput(path: String) {
    val inputStream: InputStream by lazy {
        val fileOnFileSystem = File(path)
        val returnValue: InputStream
        if(fileOnFileSystem.exists()){
            returnValue = fileOnFileSystem.inputStream()
        } else {
            val cleanedPath = if (path.startsWith("/")) path else "/" + path
            returnValue = this.javaClass.getResourceAsStream(cleanedPath)
            if (returnValue == null) {
                throw IllegalArgumentException("Cannot find file at $path on file system or classpath")
            }
        }
        returnValue
    }

    val bufferedReader: BufferedReader by lazy {
        inputStream.bufferedReader()
    }
}