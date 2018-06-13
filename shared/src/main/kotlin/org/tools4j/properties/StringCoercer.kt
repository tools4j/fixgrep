package org.tools4j.properties

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 2/05/2018
 * Time: 7:05 AM
 */
class StringCoercer(val str: String?): Coercer {
    override fun getAsDouble(): Double {
        return str!!.trim().toDouble()
    }

    override fun getAsLong(): Long {
        return str!!.trim().toLong()
    }

    override fun getAsInt(): Int {
        return str!!.trim().toInt()
    }

    override fun getAsString(): String {
        return str!!.trim()
    }

    override fun getAsBoolean(): Boolean {
        return if(str != null) str.trim().toBoolean() else false
    }

    override fun getAsDouble(default: Double): Double {
        if(str != null && !str.isEmpty()){
            return getAsDouble()
        } else {
            return default
        }
    }

    override fun getAsLong(default: Long): Long {
        if(str != null && !str.isEmpty()){
            return getAsLong()
        } else {
            return default
        }
    }

    override fun getAsInt(default: Int): Int {
        if(str != null && !str.isEmpty()){
            return getAsInt()
        } else {
            return default
        }
    }

    override fun getAsBoolean(default: Boolean): Boolean {
        if(str != null && !str.isEmpty()){
            return getAsBoolean()
        } else {
            return default
        }
    }

    override fun getAsString(default: String): String {
        if(str != null && !str.isEmpty()){
            return getAsString()
        } else {
            return default
        }
    }

    override fun getAsDoubleList(): List<Double> {
        if(str == null || str.trim().isEmpty()) return emptyList()
        return str.split(",").stream().map {it.trim()}.map { trimOfSquareBrackets(it) }.map {it.toDouble()}.collect(Collectors.toList())
    }

    override fun getAsLongList(): List<Long> {
        if(str == null || str.trim().isEmpty()) return emptyList()
        return str.split(",").stream().map {it.trim()}.map { trimOfSquareBrackets(it) }.map {it.toLong()}.collect(Collectors.toList())
    }

    override fun getAsIntList(): List<Int> {
        if(str == null || str.trim().isEmpty()) return emptyList()
        return str.split(",").stream().map {it.trim()}.map { trimOfSquareBrackets(it) }.map {it.toInt()}.collect(Collectors.toList())
    }

    override fun getAsBooleanList(): List<Boolean> {
        if(str == null || str.trim().isEmpty()) return emptyList()
        return str.split(",").stream().map {it.trim()}.map { trimOfSquareBrackets(it) }.map {it.toBoolean()}.collect(Collectors.toList())
    }

    override fun getAsStringList(): List<String> {
        if(str == null || str.trim().isEmpty()) return emptyList()
        return str.split(",").stream().map {it.trim()}.map { trimOfSquareBrackets(it) }.collect(Collectors.toList())
    }

    fun trimOfSquareBrackets(str:String): String{
        var returnStr = str.trim()
        if(returnStr.startsWith("[")){
            returnStr = returnStr.trimStart('[')
        }
        if(returnStr.endsWith("]")){
            returnStr = returnStr.trimEnd(']')
        }
        return returnStr
    }

    override fun getAsDoubleList(default: List<Double>): List<Double> {
        if(str != null && !str.trim().isEmpty()){
            return getAsDoubleList()
        } else {
            return default
        }
    }

    override fun getAsLongList(default: List<Long>): List<Long> {
        if(str != null && !str.trim().isEmpty()){
            return getAsLongList()
        } else {
            return default
        }
    }

    override fun getAsIntList(default: List<Int>): List<Int> {
        if(str != null && !str.trim().isEmpty()){
            return getAsIntList()
        } else {
            return default
        }
    }

    override fun getAsBooleanList(default: List<Boolean>): List<Boolean> {
        if(str != null && !str.trim().isEmpty()){
            return getAsBooleanList()
        } else {
            return default
        }
    }

    override fun getAsStringList(default: List<String>): List<String> {
        if(str != null && !str.trim().isEmpty()){
            return getAsStringList()
        } else {
            return default
        }
    }
}