package org.tools4j.fixgrep.help

import java.io.File

/**
 * User: ben
 * Date: 8/05/2018
 * Time: 5:58 PM
 */
class ExampleAppPropertiesFileCreator {
    val homeDir = File(System.getProperty("user.home"))
    val fixgrepHomeDir = File(homeDir.absolutePath + File.separator + ".fixgrep")
    val fixgrepAppProperties = File(fixgrepHomeDir.absolutePath + File.separator + "application.properties")

    fun createIfNecessary() {
        if(fixgrepAppProperties.exists()){
            println("Fixgrep properties file already exists at: " + fixgrepAppProperties.absolutePath)
            return
        }

        println("Attempting to create application.properties file at: " + fixgrepAppProperties.absolutePath + "\nPlease modify config in this file to change the behaviour of fixgrep for every time it is run.")
        try {
            if (!fixgrepHomeDir.exists()) {
                fixgrepHomeDir.mkdirs()
            } else {
                if(!fixgrepHomeDir.isDirectory){
                    throw RuntimeException(fixgrepHomeDir.absolutePath + " already exists, but it is a file, it must be a directory!")
                }
            }

            fixgrepAppProperties.writeText(ExampleAppPropertiesGenerator().content)
            return

        } catch(e: Exception){
            println("Could not create application.properties  reason:[" + e.message + "]")
            return
        }
    }
}