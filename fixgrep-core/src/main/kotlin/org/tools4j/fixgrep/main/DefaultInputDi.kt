package org.tools4j.fixgrep.main

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

    override val lineReader: LineReader by lazy {
        if (diContext.config.pipedInput && System.`in` != null) {
            BufferedLineReader(System.`in`)
        } else {
            fromFilesSpecifiedAsArguments()
        }
    }

    private fun fromFilesSpecifiedAsArguments(): LineReader {
        if(diContext.args.isEmpty()){
            throw IllegalArgumentException("File list empty.  Must received piped input, or specify one or more files as arguments.")
        }
        val inputStreams = ArrayList<InputStream>()
        FixGrep.logger.info("About to read from files ${diContext.args}")
        for(arg in diContext.args){
            if(arg.isEmpty()) continue
            if(arg.startsWith("-")){
                throw IllegalArgumentException("Invalid argument [$arg]")
            }
            val file = File(arg.toString())
            if(!file.exists()){
                throw IllegalArgumentException("File at location: [" + file.absolutePath + "] does not exist.")
            }
            inputStreams.add(file.inputStream())
        }
        return CompositeLineReader(inputStreams.map { BufferedLineReader(it.bufferedReader()) })
    }
}