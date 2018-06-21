package org.tools4j.properties

import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * User: ben
 * Date: 24/10/17
 * Time: 6:18 AM
 */
class ConfigImpl : Config {
    private val ESCAPED_VARIABLE_PATTERN = java.util.regex.Pattern.compile("\\\\(\\$\\{)")
    private val properties: Map<String, String?>
    constructor(repo: Config) : this(repo.asMap()) {}

    constructor(): this(LinkedHashMap<String, String>())

    constructor(properties: Map<String, String?>) {
        this.properties = LinkedHashMap()
        this.properties.putAll(properties)
    }

    override fun toPrettyString(): String {
        val sb = StringBuilder()
        for(key in properties.keys){
            sb.append(key).append("=").append(properties[key]).append("\n")
        }
        return sb.toString()
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

    constructor(key: String, value: String) {
        this.properties = LinkedHashMap()
        this.properties.put(key, value)
    }

    override fun getWithPrefix(prefix: String): Config {
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
        return ConfigImpl(returnProperties)
    }

    override fun size(): Int {
        return properties.size
    }

    override operator fun get(key: String): String? {
        return cleanValueOfEscapedVariables(properties[key])
    }

    override fun keySet(): Set<String> {
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

    override operator fun get(key: String, defaultValue: String): String? {
        val value = get(key)
        return if (value == null) {
            defaultValue
        } else {
            cleanValueOfEscapedVariables(value)
        }
    }

    override fun overrideWith(other: Config?): Config {
        if(other == null) return this
        val map: MutableMap<String, String?> = LinkedHashMap(properties)
        map.putAll(other.asMap())
        return ConfigImpl(map)
    }

    override fun toString(): String {
        return "PropertiesRepo{" +
                "properties=" + properties +
                '}'.toString()
    }

    override fun asMap(): Map<String, String?> {
        return LinkedHashMap<String, String?>(properties)
    }

    override fun resolveVariablesWithinValues(): Config {
        return ConfigImpl(ResolvedMap(asMap()).resolve())
    }

    override fun resolveVariablesWithinValues(vararg additionalPropertiesToHelpWithResolution: Config): Config {
        val additionalProperties = LinkedHashMap<String, String?>()
        for (i in additionalPropertiesToHelpWithResolution.indices) {
            additionalProperties.putAll(additionalPropertiesToHelpWithResolution[i].asMap())
        }
        return ConfigImpl(ResolvedMap(asMap(), additionalProperties).resolve())
    }

    override fun toPrettyString(indent: String): String {
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

    override fun getMandatory(key: String, message: String): String {
        return if (!properties.containsKey(key)) {
            throw IllegalStateException(message)
        } else {
            properties[key]!!
        }
    }

    override fun getAsDouble(key: String): Double{
        return StringCoercer(get(key)).getAsDouble()
    }

    override fun getAsLong(key: String): Long{
        return StringCoercer(get(key)).getAsLong()
    }

    override fun getAsInt(key: String): Int{
        return StringCoercer(get(key)).getAsInt()
    }

    override fun getAsBoolean(key: String): Boolean{
        if(properties.containsKey(key) && get(key) == null) return true
        else return StringCoercer(get(key)).getAsBoolean()
    }

    override fun getAsString(key: String): String{
        return StringCoercer(get(key)).getAsString()
    }

    override fun getAsDouble(key: String, default: Double): Double{
        return StringCoercer(get(key)).getAsDouble(default)
    }

    override fun getAsLong(key: String, default: Long): Long{
        return StringCoercer(get(key)).getAsLong(default)
    }

    override fun getAsInt(key: String, default: Int): Int{
        return StringCoercer(get(key)).getAsInt(default)
    }

    override fun getAsBoolean(key: String, default: Boolean): Boolean{
        if(properties.containsKey(key) && get(key) == null) return true
        else return StringCoercer(get(key)).getAsBoolean(default)
    }

    override fun getAsString(key: String, default: String): String{
        return StringCoercer(get(key)).getAsString(default)
    }

    override fun getAsDoubleList(key: String): List<Double>{
        return StringCoercer(get(key)).getAsDoubleList()
    }

    override fun getAsLongList(key: String): List<Long>{
        return StringCoercer(get(key)).getAsLongList()
    }

    override fun getAsIntList(key: String): List<Int>{
        return StringCoercer(get(key)).getAsIntList()
    }

    override fun getAsBooleanList(key: String): List<Boolean>{
        return StringCoercer(get(key)).getAsBooleanList()
    }

    override fun getAsStringList(key: String): List<String>{
        return StringCoercer(get(key)).getAsStringList()
    }

    override fun getAsDoubleList(key: String, default: List<Double>): List<Double>{
        return StringCoercer(get(key)).getAsDoubleList(default)
    }

    override fun getAsLongList(key: String, default: List<Long>): List<Long>{
        return StringCoercer(get(key)).getAsLongList(default)
    }

    override fun getAsIntList(key: String, default: List<Int>): List<Int>{
        return StringCoercer(get(key)).getAsIntList(default)
    }

    override fun getAsBooleanList(key: String, default: List<Boolean>): List<Boolean>{
        return StringCoercer(get(key)).getAsBooleanList(default)
    }

    override fun  hasProperty(key: String): Boolean {
        return properties.containsKey(key)
    }

    override fun hasPropertyAndIsNotFalse(key: String): Boolean {
        return hasProperty(key) && (get(key) == null || getAsString(key).toLowerCase() != "false")
    }

    override fun getAsStringList(key: String, default: List<String>): List<String>{
        return StringCoercer(get(key)).getAsStringList(default)
    }
}
