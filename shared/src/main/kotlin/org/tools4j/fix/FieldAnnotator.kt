package org.tools4j.fix

import org.tools4j.fix.spec.FixSpecDefinition

/**
 * User: ben
 * Date: 11/7/17
 * Time: 5:19 PM
 */
class FieldAnnotator(val fixSpec: FixSpecDefinition, val fieldEnumAnnotationAugmenter: FieldEnumAnnotationAugmenter){
    constructor(fixSpec: FixSpecDefinition): this(fixSpec, DefaultFieldEnumAugmentor())

    fun getField(field: Field): Field {
        return FieldImpl(getTag(field.tag), getValue(field.tag, field.value))
    }

    private fun getTag(tag: Tag): Tag {
        val fieldName: String? = fixSpec.fieldsByNumber[tag.number]?.name
        return if (fieldName != null) {
            AnnotatedTag(tag.number, fieldName)
        } else {
            tag
        }
    }

    private fun getValue(tag: Tag, value: Value): Value {
        val enumAnnotation = fieldEnumAnnotationAugmenter.lookupEnumOverride(tag.number, value.valueRaw)
                                    ?: fixSpec.fieldsByNumber.get(tag.number)?.enumsByCode?.get(value.valueRaw)
        return if (enumAnnotation != null) {
            AnnotatedValue(value.valueRaw, fieldEnumAnnotationAugmenter.augmentEnum(enumAnnotation))
        } else {
            value
        }
    }
}
