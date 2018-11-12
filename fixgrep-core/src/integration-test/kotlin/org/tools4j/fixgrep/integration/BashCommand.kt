package org.tools4j.fixgrep.integration

import mu.KLogging
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class BashCommand(val timeoutMs: Long, val command: String) {
    companion object: KLogging()

    lateinit var stdOut: String
    lateinit var stdErr: String
    var result = 0
    var timedOut = false
    val success: Boolean by lazy {result == 0}

    fun run(): BashCommand {
        val cygBinPath = System.getenv().get("CYGWIN_BIN_PATH")
        val path = System.getenv().get("Path")
        if(cygBinPath == null){
            throw IllegalStateException("Cannot find environment variable CYGWIN_BIN_PATH.  Please ensure this is configured before running any Bash tests")
        }
        println("CYGWIN_BIN_PATH: $cygBinPath")
        val env = arrayOf("PATH=$path;$cygBinPath")
        val args = listOf("${cygBinPath}/bash.exe", "-l", "-c", "\"" + command + "\"")

        val processBuilder = ProcessBuilder(args);
        logger.info { "Executing commands: " + args.joinToString(" ")}
        val proc = processBuilder.start();

        val errGobbler = StreamGobbler(proc.errorStream, logger::error)
        val outGobbler = StreamGobbler(proc.inputStream, logger::info)

        logger.info { "Starting stream gobblers (if we don't drain the output streams, then process can be blocked...)" }
        errGobbler.start()
        outGobbler.start()

        logger.info { "Waiting ${timeoutMs}ms for the command to complete executing." }
        timedOut = !(proc.waitFor(timeoutMs, TimeUnit.MILLISECONDS))
        if(timedOut) logger.warn { "Command timed out." }
        result = if(timedOut) 0  else proc.exitValue()

        logger.info { "Joining back to stream gobblers" }
        errGobbler.join()
        outGobbler.join()

        logger.info { "Completed running of command" }
        stdErr = errGobbler.getOutput()
        stdOut = outGobbler.getOutput()
        return this
    }

    class StreamGobbler(val inputStream: InputStream, val log: (msg: () -> Any?) -> Any? ) {
        private val STREAM_CLOSING_LEEWAY_MS = 1000L
        private val outputBuffer = StringBuilder()
        private var thread = Thread {
            val br = BufferedReader(InputStreamReader(inputStream))
            while (true) {
                val line = br.readLine() ?: break
                log { line }
                outputBuffer.append(line).append("\n")
            }
        }

        fun start() {
            thread.start()
        }

        fun join(){
            try {
                thread.join(STREAM_CLOSING_LEEWAY_MS)
            } catch (e: Exception){
                logger.error { "reading thread was interrupted" }
            }
        }

        fun getOutput(): String{
            return outputBuffer.toString()
        }

        fun hasOutput() = !outputBuffer.isEmpty();
    }
}