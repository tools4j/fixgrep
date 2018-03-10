package org.tools4j.properties

import java.util.HashMap

/**
 * User: ben
 * Date: 15/12/2016
 * Time: 5:37 PM
 */
enum class PropertyKeysAndDefaultValues private constructor(val key: String, private val defaultValue: String?) {
    SUPPRESS_NON_MSG_TYPE_HEADER_FIELDS("suppress.non.msg.type.header.fields", "true"),
    SUPPRESS_TRAILER_FIELDS("suppress.trailer.fields", "true"),
    FIELDS_TO_SUPPRESS("fields.to.suppress", null),
    FIELDS_TO_LIST_FIRST("fields.to.list.first", "35, 150"),
    DELIMITER_REGEX("delimiter.regex", "\\|");

    companion object {
        private val propertyLookup: MutableMap<String, String?> by lazy {
            val returnMap = HashMap<String, String?>()
            for (entry in PropertyKeysAndDefaultValues.values()) {
                returnMap[entry.key] = entry.defaultValue
            }
            returnMap
        }

        val asPropertySource: PropertySource
            get() = PropertySourceImpl(propertyLookup)

        fun containsKey(key: String): Boolean = propertyLookup.containsKey(key)
    }
}
