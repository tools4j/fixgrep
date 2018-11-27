package org.tools4j.fix

/**
 * User: benjw
 * Date: 27/11/2018
 * Time: 06:24
 */
interface FieldEnumAnnotationAugmenter {
    fun lookupEnumOverride(tag: Int, valueRaw: String): String?;
    fun augmentEnum(enumValue: String): String;

    class DoNothingFieldEnumAnnotationAugmenter: FieldEnumAnnotationAugmenter {
        override fun augmentEnum(enumValue: String): String {
            return enumValue;
        }

        override fun lookupEnumOverride(tag: Int, valueRaw: String): String? {
            return null;
        }
    }
}