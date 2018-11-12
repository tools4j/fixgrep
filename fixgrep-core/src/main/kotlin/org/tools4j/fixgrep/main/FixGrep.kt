package org.tools4j.fixgrep.main

import mu.KotlinLogging
import org.tools4j.fixgrep.help.HelpGenerator
import java.io.InputStream
import java.io.OutputStream


/**
 * User: ben
 * Date: 12/03/2018
 * Time: 7:00 AM
 */
class FixGrep(val diContext: DiContext, val inputDi: InputDi, val outputDi: OutputDi) {
    constructor(args: List<String>): this(DiContext(args.toList()))
    constructor(args: List<String>, inputDi: InputDi, outputDi: OutputDi): this(DiContext(args.toList()), inputDi, outputDi)
    constructor(args: List<String>, inputStream: InputStream, outputStream: OutputStream): this(DiContext(args.toList()), SimpleInputDi(inputStream), SimpleOutputDi(outputStream))
    constructor(args: List<String>, outputStream: OutputStream): this(DiContext(args.toList()), SimpleOutputDi(outputStream))
    constructor(diContext: DiContext, outputDi: OutputDi): this(diContext, DefaultInputDi(diContext), outputDi)
    constructor(diContext: DiContext): this(diContext, DefaultInputDi(diContext), DefaultOutputDi(diContext))

    init {
        CommandDi(diContext, inputDi, outputDi)
        diContext.addShutdown { outputDi.flushAndClose() }
    }

    companion object {
        val logger = KotlinLogging.logger {}
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                FixGrep(args.toList()).go()
            } catch (e: Exception){
                FixGrep.logger.error { e }
                System.err.println("Error: ${e.message}")
                HelpGenerator(System.out).go();
                System.exit(-1);
            }
        }
    }

    fun go() {
        diContext.go()
    }
}