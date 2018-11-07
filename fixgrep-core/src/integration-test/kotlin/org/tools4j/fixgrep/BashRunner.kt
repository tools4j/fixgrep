package org.tools4j.fixgrep

import mu.KLogging
import org.junit.jupiter.api.fail
import org.tools4j.fixgrep.main.FixGrep.Companion.logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit


/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class BashRunner {
    companion object: KLogging()

    fun run(timeoutMs: Long, command: String) {
        val cygBinPath = System.getenv().get("CYGWIN_BIN_PATH")
        val path = System.getenv().get("Path")
        if(cygBinPath == null){
            throw IllegalStateException("Cannot find environment variable CYGWIN_BIN_PATH.  Please ensure this is configured before running any Bash tests")
        }
        println("CYGWIN_BIN_PATH: $cygBinPath")
        val env = arrayOf("PATH=$path;$cygBinPath")
        val args = listOf("${cygBinPath}/bash.exe", "-l", "-c", "\"" + command + "\"")
        println("Running commands: " + args.joinToString(" "))

        val processBuilder = ProcessBuilder(args);
        val proc = processBuilder.start();
        val success = proc.waitFor(timeoutMs, TimeUnit.MILLISECONDS)
        if(!success) logger.error { "Timed out" }

        println("Return value: $success")
        val errorStream = BufferedReader(InputStreamReader(proc.errorStream))
        while (errorStream.ready())
            System.err.println(errorStream.readLine())
        val br = BufferedReader(InputStreamReader(proc.inputStream))
        while (br.ready())
            println(br.readLine())
    }
}