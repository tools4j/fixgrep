package org.tools4j.fixgrep.html

import org.tools4j.fix.FileOnClasspath
import java.io.File

/**
 * User: ben
 * Date: 16/05/2018
 * Time: 7:04 AM
 */
class CssFile {
    fun copyToWorkingDirIfRequired(){
        val outputFile = File("fixgrep.css")
        if(!outputFile.exists()){
            FileOnClasspath("fixgrep.css").writeTo(outputFile)
        }
    }
}