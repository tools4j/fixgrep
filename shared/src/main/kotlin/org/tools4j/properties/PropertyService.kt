package org.tools4j.properties

import org.tools4j.fix.FileUtils

import java.io.IOException
import java.io.StringReader
import java.util.HashMap
import java.util.Properties

/**
 * User: ben
 * Date: 20/12/2016
 * Time: 5:27 PM
 */
class PropertyService {
    private val fileUtils = FileUtils()

    val systemProperties: PropertySource
        get() {
            val properties = HashMap<String, String>()
            System.getProperties().keys.forEach { key ->
                if (PropertyKeysAndDefaultValues.containsKey(key.toString())) {
                    properties[key.toString()] = System.getProperty(key.toString())
                }
            }
            return PropertySourceImpl(properties)
        }

    val defaultProperties: PropertySource
        get() = PropertyKeysAndDefaultValues.asPropertySource

    fun getFileProperties(filePath: String): PropertySource? {
        val fileContent = fileUtils.getFileContent(filePath)
        return getPropertiesFromString(fileContent)
    }

    fun getClasspathProperties(filePath: String): PropertySource? {
        val fileContent = fileUtils.getFileContentFromClasspath(filePath)
        return getPropertiesFromString(fileContent)
    }

    private fun getPropertiesFromString(fileContent: String?): PropertySource? {
        if (fileContent == null) {
            return null
        }
        val propertiesFromFile = Properties()
        try {
            propertiesFromFile.load(StringReader(fileContent))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        val properties = HashMap<String, String>()
        propertiesFromFile.keys.forEach { key -> properties[key.toString()] = System.getProperty(key.toString()) }
        return PropertySourceImpl(properties)
    }

    fun getCommandLineProperties(args: Array<String>): PropertySource {
        val properties = HashMap<String, String>()
        for (arg in args) {
            if (!arg.contains("=")) {
                continue
            }
            val argParts = arg.trim { it <= ' ' }.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val key: String
            val value: String
            key = argParts[0].trim { it <= ' ' }

            if (argParts.size == 1) {
                value = ""
            } else if (argParts.size == 2) {
                value = argParts[1].trim { it <= ' ' }
            } else {
                value = arg.replaceFirst("^.*?=".toRegex(), "").trim { it <= ' ' }
            }
            if (PropertyKeysAndDefaultValues.containsKey(key)) {
                properties[key] = value
            }
        }
        return PropertySourceImpl(properties)
    }

    fun getProperties(commandLineArgs: Array<String>): PropertySource {
        val systemProperties = systemProperties
        val commandLineProperties = getCommandLineProperties(commandLineArgs)
        val defaultProperties = defaultProperties

        var finalProperties = defaultProperties
        finalProperties = finalProperties.overrideWith(systemProperties)
        finalProperties = finalProperties.overrideWith(commandLineProperties)
        return finalProperties
    }
}
