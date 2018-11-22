package org.tools4j.fixgrep.main

import mu.KLogging
import org.tools4j.fixgrep.main.FixGrep.Companion.logger
import org.tools4j.fixgrep.utils.BufferedLineReader
import org.tools4j.fixgrep.utils.CompositeLineReader
import org.tools4j.fixgrep.utils.LineReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*
import kotlin.collections.ArrayList

/**
 * User: benjw
 * Date: 29/10/2018
 * Time: 06:46
 */
class DefaultInputDi(val diContext: DiContext): InputDi {
    companion object: KLogging()

    override val lineReader: LineReader by lazy {
        fromFilesSpecifiedAsArguments() ?: BufferedLineReader(System.`in`)
    }

    private fun fromFilesSpecifiedAsArguments(): LineReader? {
        if(diContext.args.isEmpty()){
            return null
        }
        val inputStreams = ArrayList<InputStream>()
        logger.info{"About to read from files ${diContext.args}"}
        for(arg in diContext.args){
            if(arg.isEmpty()) continue
            if(arg.startsWith("-")){
                throw IllegalArgumentException("Invalid argument [$arg]")
            }
            val file = File(arg)
            if(!file.exists()){
                throw IllegalArgumentException("File at location: [" + file.absolutePath + "] does not exist.")
            }
            inputStreams.add(file.inputStream())
        }
        return if(inputStreams.isEmpty()){
            null
        } else {
            val compositeLineReader = CompositeLineReader(inputStreams.map { BufferedLineReader(it.bufferedReader()) })
            diContext.addShutdown {
                compositeLineReader.close()
            }
            return compositeLineReader
        }
    }
}