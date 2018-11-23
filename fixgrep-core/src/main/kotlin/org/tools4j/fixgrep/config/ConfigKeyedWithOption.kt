package org.tools4j.fixgrep.config

import org.tools4j.properties.Config

/**
 * User: ben
 * Date: 4/05/2018
 * Time: 9:16 AM
 */
open class ConfigKeyedWithOption(val config: Config) {
    init {
        config.validateAllPropertyKeysAreOneOf(Option.values().map{it.key})
    }

    operator fun get(option: Option): String? = config.get(option.key)
    operator fun get(option: Option, defaultValue: String): String? = config.get(option.key, defaultValue)
    
    fun getMandatory(option: Option, message: String = "Property '${option.key}' must be specified."): String = config.getMandatory(option.key, message)
    fun getAsDouble(option: Option): Double = config.getAsDouble(option.key)
    fun getAsLong(option: Option): Long = config.getAsLong(option.key)
    fun getAsInt(option: Option): Int = config.getAsInt(option.key)
    fun getAsBoolean(option: Option): Boolean = config.getAsBoolean(option.key)
    fun getAsString(option: Option): String = config.getAsString(option.key)
    fun getAsDouble(option: Option, default: Double): Double = config.getAsDouble(option.key, default)
    fun getAsLong(option: Option, default: Long): Long = config.getAsLong(option.key, default)
    fun getAsInt(option: Option, default: Int): Int = config.getAsInt(option.key, default)
    fun getAsBoolean(option: Option, default: Boolean): Boolean = config.getAsBoolean(option.key, default)
    fun getAsString(option: Option, default: String): String = config.getAsString(option.key, default)
    fun getAsStringOrNull(option: Option): String? = config.getAsStringOrNull(option.key)
    fun getAsDoubleList(option: Option): List<Double> = config.getAsDoubleList(option.key)
    fun getAsLongList(option: Option): List<Long> = config.getAsLongList(option.key)
    fun getAsIntList(option: Option): List<Int> = config.getAsIntList(option.key)
    fun getAsBooleanList(option: Option): List<Boolean> = config.getAsBooleanList(option.key)
    fun getAsStringList(option: Option): List<String> = config.getAsStringList(option.key)
    fun getAsDoubleList(option: Option, default: List<Double>?): List<Double>? = config.getAsDoubleList(option.key, default)
    fun getAsLongList(option: Option, default: List<Long>?): List<Long>? = config.getAsLongList(option.key, default)
    fun getAsIntList(option: Option, default: List<Int>?): List<Int>? = config.getAsIntList(option.key, default)
    fun getAsBooleanList(option: Option, default: List<Boolean>?): List<Boolean>? = config.getAsBooleanList(option.key, default)
    fun getAsStringList(option: Option, default: List<String>): List<String>? = config.getAsStringList(option.key, default)
    fun hasProperty(option: Option): Boolean = config.hasProperty(option.key)
    fun toPrettyString(): String = config.toPrettyString()
    fun hasPropertyAndIsNotFalse(option: Option): Boolean = config.hasPropertyAndIsNotFalse(option.key)
    fun hasPropertyAndIsTrueOrNull(option: Option): Boolean  = config.hasPropertyAndIsTrueOrNull(option.key)
}