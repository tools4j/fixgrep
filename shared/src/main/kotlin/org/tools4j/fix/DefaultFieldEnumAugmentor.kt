package org.tools4j.fix

/**
 * User: benjw
 * Date: 27/11/2018
 * Time: 06:33
 */
class DefaultFieldEnumAugmentor: FieldEnumAnnotationAugmenter {
    val overrides = HashMap<String, String>()
    init {
        overrides.put("150=F", "FILL")
    }

    override fun lookupEnumOverride(tag: Int, valueRaw: String): String? {
        return overrides.get("$tag=$valueRaw")
    }

    override fun augmentEnum(enumValue: String): String {
        return enumValue
    }
}