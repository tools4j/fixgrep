package org.tools4j.properties

import java.util.HashMap

/**
 * This class will _repeat_ any properties with 'Constant Style' keys and replace with 'Property Style' keys.
 * e.g. if a property exists "JAVA_HOME", an _additional_ property with key "java.home" will
 * be added to the PropertiesRepo.
 *
 * The load method looks for any properties whose key starts with an upper-case letter and only
 * consists of upper-case letters and underscores.
 *
 * This class exists so that environment variables can be used to override Java properties.  In the
 * case of Linux (and I maybe Mac) environment variables cannot be specified with dots ".".  In such
 * cases, the user/configurer can use underscores (and capital letters) instead.  And the keys will
 * be transformed.
 */
class ConfigWithAdditionalTweakedContantStyleProperties(private val source: Config) : ConfigSource {
    override val config: Config by lazy {
        val properties: MutableMap<String,String> = HashMap(source.asMap())
        val convertedProperties: MutableMap<String, String> = HashMap()
        properties.keys.stream().filter {isScreamingSnakeCase(it)}.forEach {
            convertedProperties[toPropertiesStyle(it)] = properties[it]!!
        }
        properties.putAll(convertedProperties)
        ConfigImpl(properties)
    }

    private fun toPropertiesStyle(key: String): String {
        return key.toLowerCase().replace("_".toRegex(), ".")
    }

    private fun isScreamingSnakeCase(key: String): Boolean {
        return key.matches("[A-Z]+?[[A-Z]_]*".toRegex())
    }
}
