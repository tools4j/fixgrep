package org.tools4j.fixgrep.help

import java.io.File

/**
 * User: ben
 * Date: 8/05/2018
 * Time: 5:58 PM
 */
class ExampleAppPropertiesFileCreator {
    companion object {
        val HOME_DIR = File(System.getProperty("user.home"))
        val FIXGREP_HOME_DIR = File(HOME_DIR.absolutePath + File.separator + ".fixgrep")
        val FIXGREP_APP_PROPERTIES_FILE = File(FIXGREP_HOME_DIR.absolutePath + File.separator + "application.properties")
    }

    fun createIfNecessary() {
        if(FIXGREP_APP_PROPERTIES_FILE.exists()){
            println("Fixgrep properties file already exists at: " + FIXGREP_APP_PROPERTIES_FILE.absolutePath)
            return
        }

        println("Attempting to create application.properties file at: " + FIXGREP_APP_PROPERTIES_FILE.absolutePath + "\nPlease modify config in this file to change the behaviour of fixgrep for every time it is run.")
        try {
            if (!FIXGREP_HOME_DIR.exists()) {
                FIXGREP_HOME_DIR.mkdirs()
            } else {
                if(!FIXGREP_HOME_DIR.isDirectory){
                    throw RuntimeException(FIXGREP_HOME_DIR.absolutePath + " already exists, but it is a file, it must be a directory!")
                }
            }

            FIXGREP_APP_PROPERTIES_FILE.writeText(ExampleAppPropertiesGenerator().content)
            return

        } catch(e: Exception){
            println("Could not create application.properties  reason:[" + e.message + "]")
            return
        }
    }
}