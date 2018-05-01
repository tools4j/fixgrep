package org.tools4j.fix

/**
 * User: ben
 * Date: 4/04/2018
 * Time: 5:58 PM
 */
class AnnotatedField(val field: Field, val spec: AnnotationSpec): Field by field {
    override fun toPrettyString(): String {
        val sb = StringBuilder()

        if(spec.annotationPositions.neitherTagNorValueAnnotated){
            sb.append(tag.tag)
        } else if(!(field.tag is AnnotatedTag)){
            if (spec.boldTagAndValue) sb.append(AnnotatedField.Bold)
            sb.append(tag.tag)
            if (spec.boldTagAndValue) sb.append(AnnotatedField.Normal)
        } else {
            sb.append((field.tag as AnnotatedTag).toAnnotatedString(spec.annotationPositions.tagAnnotationPosition, spec.boldTagAndValue))
        }

        sb.append("=")

        if(spec.annotationPositions.neitherTagNorValueAnnotated){
            sb.append(value.rawValue)
        } else if(!(field.value is AnnotatedValue)){
            if (spec.boldTagAndValue) sb.append(AnnotatedField.Bold)
            sb.append(value.rawValue)
            if (spec.boldTagAndValue) sb.append(AnnotatedField.Normal)
        } else {
            sb.append((field.value as AnnotatedValue).toAnnotatedString(spec.annotationPositions.valueAnnotationPosition, spec.boldTagAndValue))
        }

        return sb.toString()
    }

    companion object {
        val Bold = "\u001B[1m"
        val Normal = "\u001B[22m"
    }

    enum class AnnotationPosition{
        BEFORE,
        AFTER,
        NONE;
    }
}