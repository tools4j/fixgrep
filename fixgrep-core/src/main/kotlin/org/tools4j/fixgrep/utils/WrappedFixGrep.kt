package org.tools4j.fixgrep.utils

import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.ResourceGroovyMethods
import org.tools4j.fixgrep.help.HelpGenerator.Companion.fix
import org.tools4j.fixgrep.main.FixGrep
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.utils.ArgsAsString

import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * User: benjw
 * Date: 30/10/2018
 * Time: 17:09
 */
class WrappedFixGrep @JvmOverloads constructor(private val args: List<String>, private val launchResultInBrowser: Boolean = false, private val logResultsToFile: Boolean = false) {

    constructor(args: String) : this(ArgsAsString(args).toArgs())
    constructor(args: String = "", launchResultInBrowser: Boolean = false, logResultsToFile: Boolean = false) : this(ArgsAsString(args).toArgs(), launchResultInBrowser, logResultsToFile)

    companion object {
        private val RESULTS_FILE = File("results.txt")
    }

    init {
        if (logResultsToFile) deleteAndCreateNewFile(RESULTS_FILE)
    }

    fun go(): String {
        return go(emptyList(), fix)
    }

    fun go(args: String, fix: String): String{
        return go(ArgsAsString(args).toArgs(), fix)
    }

    fun go(fix: String): String {
        return go(emptyList(), fix)
    }

    fun go(args: List<String> = emptyList(), fix: String = ""): String {
        //Add the more specific args first (first ones take presendence)
        val allArgs = ArrayList(args)

        //Add the more 'generic' ones second
        allArgs.addAll(this.args)

        try {
            allArgs.addAll(this.args)
            val input = CircularBufferedReaderWriter()
            val output = CircularBufferedReaderWriter()

            if (launchResultInBrowser) {
                val browserLaunchInput = CircularBufferedReaderWriter()
                val browserLaunchOutput = CircularBufferedReaderWriter()

                browserLaunchInput.writer.write(fix)
                browserLaunchInput.writer.flush()
                browserLaunchInput.writer.close()

                val argsListWithLaunchBrowserFlag = ArrayList(allArgs)
                argsListWithLaunchBrowserFlag.add("-l")
                val result = FixGrep(argsListWithLaunchBrowserFlag, browserLaunchInput.inputStream, browserLaunchOutput.outputStream).go()
                if(result != 0) throw RuntimeException("Error occurred whilst running FixGrep")
            }

            input.writer.write(fix)
            input.writer.flush()
            input.writer.close()

            val fixGrep = FixGrep(allArgs, input.inputStream, output.outputStream)
            val result = fixGrep.go()
            if(result != 0) throw RuntimeException("Error occurred whilst running FixGrep")

            output.outputStream.flush()
            val lines = output.readLines("\n")

            if (logResultsToFile) {
                ResourceGroovyMethods.append(RESULTS_FILE, allArgs)
                ResourceGroovyMethods.append(RESULTS_FILE, "\n")
                ResourceGroovyMethods.append(RESULTS_FILE, lines)
                ResourceGroovyMethods.append(RESULTS_FILE, "\n")
                ResourceGroovyMethods.append(RESULTS_FILE, "\n")
            }
            return lines

        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    private fun deleteAndCreateNewFile(file: File) {
        try {
            if (DefaultGroovyMethods.asBoolean(file)) {
                if (file.exists()) {
                    file.delete()
                }

                file.createNewFile()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
