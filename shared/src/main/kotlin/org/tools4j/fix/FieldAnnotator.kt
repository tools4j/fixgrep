package org.tools4j.fix

/**
 * User: ben
 * Date: 11/7/17
 * Time: 5:19 PM
 */
class FieldAnnotator(
        val fixSpec: FixSpec,
        val annotationPositions: AnnotationPositions = AnnotationPositions.OUTSIDE_ANNOTATED){

    fun getField(field: Field): Field {
        return FieldImpl(getTag(field.tag), getValue(field.tag, field.value))
    }

    private fun getTag(tag: Tag): Tag {
        val tagDescription: String? = fixSpec.fieldsAndEnumValues[""+tag]
        return if (tagDescription != null) {
            AnnotatedTag(tag.tagRaw, tagDescription)
        } else {
            tag
        }
    }

    private fun getValue(tag: Tag, value: Value): Value {
        val tagDescription = fixSpec.fieldsAndEnumValues["" + tag.tagRaw + "." + value.valueRaw]
        return if (tagDescription != null) {
            AnnotatedValue(value.valueRaw, tagDescription)
        } else {
            value
        }
    }
}
