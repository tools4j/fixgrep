package org.tools4j.fixgrep.main

import org.tools4j.fixgrep.config.ConfigAndArguments
import org.tools4j.fixgrep.config.ConfigBuilder
import org.tools4j.fixgrep.config.FixGrepConfig
import org.tools4j.fixgrep.help.HelpGenerator

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:32
 */
class DiContext(private val configAndArguments: ConfigAndArguments) {
    constructor(commandLineArgs: List<String>): this(ConfigBuilder(commandLineArgs).configAndArguments)
    constructor(): this(emptyList())
    val initializations = ArrayList<() -> Unit>()
    val services = ArrayList<() -> Unit>()
    val shutdowns = ArrayList<() -> Unit>()
    val config: FixGrepConfig by lazy { FixGrepConfig(configAndArguments.config) }
    val args: List<String> by lazy { configAndArguments.arguments }

    fun go(){
        try {
            initializations.forEach { it() }
            services.forEach { it() }
            shutdowns.forEach { it() }
        } catch (e: Throwable){
            FixGrep.logger.error { e }
            System.err.println("Invalid option ${e.message}")
            HelpGenerator(System.out).go();
            return
        }
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
}
