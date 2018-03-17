package org.tools4j.properties

import org.tools4j.fix.FileResource
import org.tools4j.fix.SplitableByRegexString

import java.io.BufferedReader
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.HashSet
import kotlin.collections.HashMap

/**
 * User: ben
 * Date: 29/6/17
 * Time: 5:39 PM
 */
class PropertiesFile(private val fileResource: FileResource) {

    val mapValue: Map<String, String> by lazy {
        try {
            val map = HashMap<String, String?>()
            val inputStream = DataInputStream(fileResource.asInputStream())
            val br = BufferedReader(InputStreamReader(inputStream))
            while(true) {
                val line = br.readLine() ?: break
                val tagAndValue = SplitableByRegexString(line, "=").split()
                val tag = tagAndValue[0]
                val value = tagAndValue.allElementsOnwards(1, ",")
                map[tag] = value
            }
            HashMap<String, String>(map)
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }

    }

    val setValue: Set<String> by lazy {
        try {
            val set = HashSet<String>()
            val inputStream = DataInputStream(fileResource.asInputStream())
            val br = BufferedReader(InputStreamReader(inputStream))
            while (true){
                val line = br.readLine() ?: break
                set.add(line)
            }
            HashSet<String>(set)
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }
}
