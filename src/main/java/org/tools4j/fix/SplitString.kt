package org.tools4j.fix


import java.util.Arrays

/**
 * User: ben
 * Date: 13/06/2017
 * Time: 6:49 AM
 */
class SplitString(private val splitValues: Array<String>?) : Iterable<String> {

    fun allElementsOnwards(indexIncludingAndOnwards: Int, delimiter: String): String? {
        if (splitValues!!.size <= indexIncludingAndOnwards) {
            return null
        } else if (splitValues.size == indexIncludingAndOnwards + 1) {
            return splitValues[indexIncludingAndOnwards]
        } else {
            val sb = StringBuilder()
            for (i in indexIncludingAndOnwards until splitValues.size) {
                if (sb.length > 0) {
                    sb.append(delimiter)
                }
                sb.append(splitValues[i])
            }
            return sb.toString()
        }
    }

    operator fun get(index: Int): String {
        return splitValues!![index]
    }

    override fun iterator(): Iterator<String> {
        if (splitValues == null) {
            throw NullPointerException("Please call split() before calling iterator().")
        }
        return Arrays.asList(*splitValues).iterator()
    }

    fun values(): Array<String>? {
        return splitValues
    }

    fun indexOf(value: String): Int {
        for (i in splitValues!!.indices) {
            if (splitValues[i] == value) {
                return i
            }
        }
        throw IllegalStateException("Value: " + value + " is not an element of array: " + Arrays.toString(splitValues))

    }

    operator fun contains(value: String): Boolean {
        for (i in splitValues!!.indices) {
            if (splitValues[i] == value) {
                return true
            }
        }
        return false
    }

    fun equalsArray(expected: Array<String>): Boolean {
        return if(expected.isEmpty() && splitValues!!.isEmpty()){
            true
        } else {
            Arrays.equals(splitValues, expected)
        }
    }

    override fun toString(): String {
        return "SplitString{" + Arrays.toString(splitValues) + '}'.toString()
    }
}
