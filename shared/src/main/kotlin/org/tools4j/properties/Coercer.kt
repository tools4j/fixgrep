package org.tools4j.properties

/**
 * User: ben
 * Date: 2/05/2018
 * Time: 7:01 AM
 */
interface Coercer {
    fun getAsDouble(): Double
    fun getAsLong(): Long
    fun getAsInt(): Int
    fun getAsBoolean(): Boolean
    fun getAsString(): String

    fun getAsDouble(default: Double): Double
    fun getAsLong(default: Long): Long
    fun getAsInt(default: Int): Int
    fun getAsBoolean(default: Boolean): Boolean
    fun getAsString(default: String): String

    fun getAsDoubleList(): List<Double>
    fun getAsLongList(): List<Long>
    fun getAsIntList(): List<Int>
    fun getAsBooleanList(): List<Boolean>
    fun getAsStringList(): List<String>

    fun getAsDoubleList(default: List<Double>): List<Double>
    fun getAsLongList(default: List<Long>): List<Long>
    fun getAsIntList(default: List<Int>): List<Int>
    fun getAsBooleanList(default: List<Boolean>): List<Boolean>
    fun getAsStringList(default: List<String>): List<String>
}