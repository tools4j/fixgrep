package org.tools4j.fix

/**
 * User: ben
 * Date: 11/7/17
 * Time: 5:19 PM
 */
class FieldAnnotator(
        val fixSpec: FixSpec,
        val annotationSpec: AnnotationSpec = AnnotationSpec.OUTSIDE_ANNOTATED_BOLD_TAG_VALUES){

    fun getField(field: Field): Field {
        return getField(field.tag.tag, field.value.rawValue)
    }

    fun getField(tagInt: Int, valueStr: String): Field {
        val tag = getTag(tagInt)
        val value = getValue(tag, valueStr)
        return AnnotatedField(tag, value, annotationSpec)
    }

    private fun getTag(tag: Int): Tag {
        val tagDescription: String? = fixSpec.fieldsAndEnumValues[""+tag]
        return if (tagDescription != null) {
            AnnotatedTag(tag, tagDescription, annotationSpec.annotationPositions.tagAnnotationPosition, annotationSpec.boldTagAndValue)
        } else {
            NonAnnotatedTag(tag, annotationSpec.boldTagAndValue)
        }
    }

    private fun getValue(tag: Tag, rawValue: String): Value {
        val tagDescription = fixSpec.fieldsAndEnumValues["" + tag.tag + "." + rawValue]
        return if (tagDescription != null) {
            AnnotatedValue(rawValue, tagDescription, annotationSpec.annotationPositions.valueAnnotationPosition, annotationSpec.boldTagAndValue)
        } else {
            NonAnnotatedValue(rawValue, annotationSpec.boldTagAndValue)
        }
    }
}
