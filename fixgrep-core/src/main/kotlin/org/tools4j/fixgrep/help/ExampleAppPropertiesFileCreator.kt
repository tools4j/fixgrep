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
    val tempDir = File(System.getProperty("java.io.tmpdir"))
    val processFailedFlagFile = File(tempDir.absolutePath + File.separator + "fixgrep-failed-app-properties-creation.txt")

    fun createIfNecessary() {
        if(fixgrepAppProperties.exists()){
            return
        }

        if(processFailedFlagFile.exists()){
            return
        }

        println("Attempting to create application.properties file at: " + fixgrepAppProperties.absolutePath + " Please modify config in this file to change the behaviour of fixgrep for every time it is run.")
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
            processFailedFlagFile.createNewFile()
            processFailedFlagFile.writeText("Could not create application.properties file at: ${fixgrepAppProperties.absolutePath}, with error message: ${e.message}.  If you wish fixgrep to try again, please delete this file, and run fixgrep again.")
            return
        }
    }

    fun deleteProcessFailedFlagFile(){
        if(processFailedFlagFile.exists()) processFailedFlagFile.delete()
    }
}