package org.tools4j.properties

/**
 * User: ben
 * Date: 4/05/2018
 * Time: 9:16 AM
 */
interface Config {
    fun getWithPrefix(prefix: String): Config
    fun size(): Int

    operator fun get(key: String): String?
    fun keySet(): Set<String>

    operator fun get(key: String, defaultValue: String): String?
    fun overrideWith(other: Config?): Config
    fun asMap(): Map<String, String?>
    fun resolveVariablesWithinValues(): Config
    fun resolveVariablesWithinValues(vararg additionalPropertiesToHelpWithResolution: Config): Config
    fun toPrettyString(indent: String = "    "): String
    fun getMandatory(key: String, message: String = "Property '$key' must be specified."): String
    fun getAsDouble(key: String): Double
    fun getAsLong(key: String): Long
    fun getAsInt(key: String): Int
    fun getAsBoolean(key: String): Boolean
    fun getAsString(key: String): String
    fun getAsDouble(key: String, default: Double): Double
    fun getAsLong(key: String, default: Long): Long
    fun getAsInt(key: String, default: Int): Int
    fun getAsBoolean(key: String, default: Boolean): Boolean
    fun getAsString(key: String, default: String): String
    fun getAsDoubleList(key: String): List<Double>
    fun getAsLongList(key: String): List<Long>
    fun getAsIntList(key: String): List<Int>
    fun getAsBooleanList(key: String): List<Boolean>
    fun getAsStringList(key: String): List<String>
    fun getAsDoubleList(key: String, default: List<Double>?): List<Double>?
    fun getAsLongList(key: String, default: List<Long>?): List<Long>?
    fun getAsIntList(key: String, default: List<Int>?): List<Int>?
    fun getAsBooleanList(key: String, default: List<Boolean>?): List<Boolean>?
    fun getAsStringList(key: String, default: List<String>?): List<String>?
    fun hasProperty(key: String): Boolean
    fun toPrettyString(): String
    fun hasPropertyAndIsNotFalse(key: String): Boolean
    fun hasPropertyAndIsTrueOrNull(key: String): Boolean {
        return get(key) == null || get(key).toString().toLowerCase().equals("true")
    }
    fun validateAllPropertyKeysAreOneOf(allowedKeys: List<String>){
        val keysPresentWhichAreNotInAllowedList = ArrayList<String>()
        for(key in keySet()){
            if(!allowedKeys.contains(key)){
                keysPresentWhichAreNotInAllowedList.add(key)
            }
        }
        if(!keysPresentWhichAreNotInAllowedList.isEmpty()) {
            throw IllegalArgumentException("The following property keys were found, which were not present in the allowed list \nallowedKeys$allowedKeys\nillegalKeys:$keysPresentWhichAreNotInAllowedList")
        }
    }

    companion object {
        fun empty(): Config {return ConfigImpl(emptyMap())}
        val PRESENT = Object()
    }
}