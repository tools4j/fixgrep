package org.tools4j.properties

import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * User: ben
 * Date: 24/10/17
 * Time: 6:18 AM
 */
class Config {
    private val ESCAPED_VARIABLE_PATTERN = java.util.regex.Pattern.compile("\\\\(\\$\\{)")
    private val properties: Map<String, String>
    constructor(repo: Config) : this(repo.asMap()) {}

    @JvmOverloads constructor(properties: Map<String, String> = LinkedHashMap()) {
        this.properties = LinkedHashMap()
        this.properties.putAll(properties)
    }

    val nextUniqueKeyParts: Set<String>
        get() {
            val uniqueKeyParts = LinkedHashSet<String>()
            for (key in properties.keys) {
                val firstPartOfKey = key.replaceFirst("\\..*".toRegex(), "")
                uniqueKeyParts.add(firstPartOfKey)
            }
            return uniqueKeyParts
        }

    constructor(properties: Properties) {
        this.properties = LinkedHashMap()
        for (key in properties.keys) {
            this.properties[key as String] = properties[key] as String
        }
    }

    fun getWithPrefix(prefix: String): Config {
        var prefix = prefix
        if (!prefix.endsWith(".")) {
            prefix += "."
        }
        val returnProperties = Properties()
        for (key in properties.keys) {
            if (key.startsWith(prefix)) {
                val newKey = key.replace(prefix, "")
                returnProperties[newKey] = properties[key]
            }
        }
        return Config(returnProperties)
    }

    fun size(): Int {
        return properties.size
    }

    operator fun get(key: String): String? {
        return cleanValueOfEscapedVariables(properties[key])
    }

    fun keySet(): Set<String> {
        val returnSet = LinkedHashSet<String>()
        for (key in properties.keys) {
            returnSet.add(key)
        }
        return returnSet
    }

    fun cleanValueOfEscapedVariables(value: String?): String? {
        var value: String? = value ?: return null
        val matcher = ESCAPED_VARIABLE_PATTERN.matcher(value!!)
        value = matcher.replaceAll("$1")
        return value
    }

    operator fun get(key: String, defaultValue: String): String? {
        val value = get(key)
        return if (value == null) {
            defaultValue
        } else {
            cleanValueOfEscapedVariables(value)
        }
    }

    fun overrideWith(other: Config?): Config {
        if(other == null) return this
        val map: MutableMap<String, String> = LinkedHashMap(properties)
        map.putAll(other.properties)
        return Config(map)
    }

    override fun toString(): String {
        return "PropertiesRepo{" +
                "properties=" + properties +
                '}'.toString()
    }

    fun asMap(): Map<String, String> {
        return LinkedHashMap(properties)
    }

    fun resolveVariablesWithinValues(): Config {
        return Config(ResolvedMap(asMap()).resolve())
    }

    fun resolveVariablesWithinValues(vararg additionalPropertiesToHelpWithResolution: Config): Config {
        val additionalProperties = LinkedHashMap<String, String>()
        for (i in additionalPropertiesToHelpWithResolution.indices) {
            additionalProperties.putAll(additionalPropertiesToHelpWithResolution[i].asMap())
        }
        return Config(ResolvedMap(asMap(), additionalProperties).resolve())
    }

    @JvmOverloads
    fun toPrettyString(indent: String = "    "): String {
        val sb = IndentableStringBuilder(indent)
        sb.append("PropertiesRepo{\n")
        sb.activateIndent()
        for (key in properties.keys) {
            sb.append(key).append("=").append(get(key)!!).append("\n")
        }
        sb.decactivateIndent()
        sb.append("}")
        return sb.toString()
    }

    @JvmOverloads
    fun getMandatory(key: String, message: String = "Property '$key' must be specified."): String {
        return if (!properties.containsKey(key)) {
            throw IllegalStateException(message)
        } else {
            properties[key]!!
        }
    }

    companion object {
        fun empty(): Config {
            return Config(emptyMap())
        }
    }

    fun getAsDouble(key: String): Double{
        return StringCoercer(get(key)).getAsDouble()
    }

    fun getAsLong(key: String): Long{
        return StringCoercer(get(key)).getAsLong()
    }

    fun getAsInt(key: String): Int{
        return StringCoercer(get(key)).getAsInt()
    }

    fun getAsBoolean(key: String): Boolean{
        return StringCoercer(get(key)).getAsBoolean()
    }

    fun getAsString(key: String): String{
        return StringCoercer(get(key)).getAsString()
    }

    fun getAsDouble(key: String, default: Double): Double{
        return StringCoercer(get(key)).getAsDouble(default)
    }

    fun getAsLong(key: String, default: Long): Long{
        return StringCoercer(get(key)).getAsLong(default)
    }

    fun getAsInt(key: String, default: Int): Int{
        return StringCoercer(get(key)).getAsInt(default)
    }

    fun getAsInt(key: String, default: Boolean): Boolean{
        return StringCoercer(get(key)).getAsBoolean(default)
    }

    fun getAsString(key: String, default: String): String{
        return StringCoercer(get(key)).getAsString(default)
    }

    fun getAsDoubleList(key: String): List<Double>{
        return StringCoercer(get(key)).getAsDoubleList()
    }

    fun getAsLongList(key: String): List<Long>{
        return StringCoercer(get(key)).getAsLongList()
    }

    fun getAsIntList(key: String): List<Int>{
        return StringCoercer(get(key)).getAsIntList()
    }

    fun getAsBooleanList(key: String): List<Boolean>{
        return StringCoercer(get(key)).getAsBooleanList()
    }

    fun getAsStringList(key: String): List<String>{
        return StringCoercer(get(key)).getAsStringList()
    }

    fun getAsDoubleList(key: String, default: List<Double>): List<Double>{
        return StringCoercer(get(key)).getAsDoubleList(default)
    }

    fun getAsLongList(key: String, default: List<Long>): List<Long>{
        return StringCoercer(get(key)).getAsLongList(default)
    }

    fun getAsIntList(key: String, default: List<Int>): List<Int>{
        return StringCoercer(get(key)).getAsIntList(default)
    }

    fun getAsBooleanList(key: String, default: List<Boolean>): List<Boolean>{
        return StringCoercer(get(key)).getAsBooleanList(default)
    }

    fun getAsStringList(key: String, default: List<String>): List<String>{
        return StringCoercer(get(key)).getAsStringList(default)
    }
}
