package org.tools4j.properties

/**
 * User: ben
 * Date: 19/12/2016
 * Time: 6:11 PM
 */
interface PropertySource {
    fun getAsString(key: String): String?
    fun getAsBoolean(key: String): Boolean?
    fun getAsInt(key: String): Int?
    fun getAsLong(key: String): Long?
    fun getAsDouble(key: String): Double?
    fun getAsList(key: String): List<String>
    fun getAsMap(key: String): Map<String, String>
    fun containsKey(key: String): Boolean
    fun overrideWith(other: PropertySource): PropertySource
}
