package org.tools4j.fixgrep.main

import ch.qos.logback.classic.Level
import mu.KLogging
import org.tools4j.fixgrep.config.ConfigAndArguments
import org.tools4j.fixgrep.config.ConfigBuilder
import org.tools4j.fixgrep.config.FixGrepConfig
import org.tools4j.fixgrep.help.HelpGenerator
import org.tools4j.fixgrep.main.FixGrep.Companion.logger
import org.tools4j.utils.Logging
import java.lang.IllegalStateException

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:32
 */
class DiContext(private val configAndArguments: ConfigAndArguments) {
    constructor(commandLineArgs: List<String>): this(ConfigBuilder(commandLineArgs).configAndArguments)
    constructor(): this(emptyList())
    companion object: KLogging()
    val initializations = ArrayList<() -> Unit>()
    val services = ArrayList<() -> Unit>()
    val shutdowns = ArrayList<() -> Unit>()
    val config: FixGrepConfig by lazy { FixGrepConfig(configAndArguments.config) }
    val args: List<String> by lazy { configAndArguments.arguments }

    fun go(){
        if(config.debugMode) Logging.setLoggingLevel(Level.DEBUG)
        logger.info{"Running initializations"}
        initializations.forEach { it() }
        logger.info{"Running services"}
        services.forEach { it() }
        logger.info{"Running shutdowns"}
        runShutdowns()
        logger.info{"Finished, exiting"}
    }

    fun addInit(initialization: () -> Unit){
       initializations.add(initialization)
    }

    fun addRunnable(service: () -> Unit){
        services.add(service)
    }

    fun addShutdown(shutdown: () -> Unit) {
        shutdowns.add(shutdown)
    }

    fun runShutdowns() {
        var lastErrorOccurringDuringShutdown: Throwable? = null
        shutdowns.forEach {
            try {
                it()
            } catch (e: Exception) {
                lastErrorOccurringDuringShutdown = e
                logger.error("Error occurred during shutdown", e)
            }
        }
        if(lastErrorOccurringDuringShutdown != null) throw IllegalStateException("One or more errors occurred during shutdown.  Last error incurred: ", lastErrorOccurringDuringShutdown)
    }
}
