package org.tools4j.properties

import java.util.HashMap

/**
 * User: ben
 * Date: 20/12/2016
 * Time: 5:17 PM
 */
class PropertySourceImpl(properties: Map<String, String?>) : PropertySourceBase() {
    private val properties: Map<String, String?>
    private var propertyOverride: PropertySource? = null

    init {
        this.properties = HashMap(properties)
        propertyOverride = null
    }

    constructor(properties: Map<String, String?>, override: PropertySource) : this(properties) {
        this.propertyOverride = null
    }

    override fun overrideWith(other: PropertySource): PropertySource {
        return PropertySourceImpl(this.properties, other)
    }

    override fun containsKey(key: String): Boolean {
        return properties.containsKey(key) || propertyOverride != null && propertyOverride!!.containsKey(key)
    }

    override fun get(key: String): String? {
        return if (propertyOverride?.containsKey(key) != null) {
            propertyOverride!!.getAsString(key)
        } else properties[key]
    }
}
