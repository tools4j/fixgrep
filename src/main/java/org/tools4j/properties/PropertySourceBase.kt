package org.tools4j.properties

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.HashMap

/**
 * User: ben
 * Date: 20/12/2016
 * Time: 10:40 AM
 */
abstract class PropertySourceBase : PropertySource {

    protected abstract operator fun get(key: String): String?

    override fun getAsString(key: String): String? {
        return get(key)
    }

    override fun getAsBoolean(key: String): Boolean? {
        val str = getAsString(key)
        return if (str == null) null else java.lang.Boolean.valueOf(key)
    }

    override fun getAsInt(key: String): Int? {
        val str = getAsString(key)
        return if (str == null) null else Integer.valueOf(key)
    }

    override fun getAsLong(key: String): Long? {
        val str = getAsString(key)
        return if (str == null) null else java.lang.Long.valueOf(key)
    }

    override fun getAsDouble(key: String): Double? {
        val str = getAsString(key)
        return if (str == null) null else java.lang.Double.valueOf(key)
    }

    override fun getAsList(key: String): List<String> {
        val str = getAsString(key)
        if (str == null) {
            return emptyList()
        } else {
            val list = ArrayList<String>()
            Arrays.asList(*str.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).forEach { item -> list.add(item.trim { it <= ' ' }) }
            return list
        }
    }

    override fun getAsMap(key: String): Map<String, String> {
        val str = getAsString(key)
        if (str == null) {
            return emptyMap()
        } else {
            val map = HashMap<String, String>()
            Arrays.asList(*str.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).forEach { entry ->
                val entryParts = entry.trim { it <= ' ' }.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                map[entryParts[0].trim { it <= ' ' }] = entryParts[1].trim { it <= ' ' }
            }
            return map
        }
    }
}
