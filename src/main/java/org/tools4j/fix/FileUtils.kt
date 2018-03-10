package org.tools4j.fix

import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * User: ben
 * Date: 20/12/2016
 * Time: 5:27 PM
 */
class FileUtils {

    fun getFileContent(filePath: String): String? {
        val errorBuffer = StringBuilder()
        var fileContent = getFileContentAtPath(filePath, errorBuffer)
        if (fileContent != null) {
            return fileContent
        }

        fileContent = getFileContentRelativeToUserHome(filePath, errorBuffer)
        if (fileContent != null) {
            return fileContent
        }

        fileContent = getFileContentFromClasspath(filePath, errorBuffer)
        if (fileContent != null) {
            return fileContent
        }

        System.err.println(errorBuffer)
        return null
    }

    fun getFileContentFromClasspath(filePath: String): String? {
        return getFileContentFromClasspath(filePath, null)

    }

    fun getFileContentRelativeToUserHome(filePath: String): String? {
        return getFileContentRelativeToUserHome(filePath, null)
    }

    fun getFileContentAtPath(filePath: String): String? {
        return getFileContentAtPath(filePath, null)
    }

    private fun getFileContentFromClasspath(filePath: String, errorBuffer: StringBuilder?): String? {
        try {
            val fileAsResource = javaClass.getResource(filePath)
            if (fileAsResource == null) {
                errorBuffer?.append("Could not find file on classpath at: ")?.append(filePath)?.append("\n")
                return null
            }
            return String(Files.readAllBytes(Paths.get(fileAsResource.toURI())))
        } catch (e: Exception) {
            errorBuffer?.append("Could not get file on classpath at: ")?.append(filePath)?.append(":")?.append(e.message)?.append("\n")
            return null
        }

    }

    private fun getFileContentRelativeToUserHome(filePath: String, errorBuffer: StringBuilder?): String? {
        var filePath = filePath
        filePath = System.getProperty("user.home") + System.getProperty("file.separator") + filePath
        return getFileContentAtPath(filePath, errorBuffer)
    }

    private fun getFileContentAtPath(filePath: String, errorBuffer: StringBuilder?): String? {
        val file = File(filePath)
        if (file.exists()) {
            try {
                val encoded = Files.readAllBytes(Paths.get(file.absolutePath))
                return String(encoded, Charset.defaultCharset())

            } catch (e: IOException) {
                errorBuffer?.append("Could not get file at: ")?.append(file.absolutePath)?.append(":")?.append(e.message)?.append("\n")
                return null
            }

        } else {
            errorBuffer?.append("Could not find file at: ")?.append(file.absolutePath)?.append("\n")
            return null
        }
    }
}
