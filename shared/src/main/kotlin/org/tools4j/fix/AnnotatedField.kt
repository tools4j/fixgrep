package org.tools4j.fix

/**
 * User: ben
 * Date: 4/04/2018
 * Time: 5:58 PM
 */
class AnnotatedField(val field: Field, val spec: AnnotationSpec): Field by field {
    override fun toPrettyString(): String {
        val sb = StringBuilder()

        if(spec.tagAnnotationPosition == AnnotatedField.AnnotationPosition.NONE){
            sb.append(tag.tag)
        } else if(spec.tagAnnotationPosition == AnnotatedField.AnnotationPosition.BEFORE){
            sb.append(tag.tagWithAnnotatedPrefix)
        } else {
            sb.append(tag.tagWithAnnotatedPostfix)
        }

        sb.append("=")

        if(spec.valueAnnotationPosition == AnnotatedField.AnnotationPosition.NONE){
            sb.append(value.rawValue)
        } else if(spec.valueAnnotationPosition == AnnotationPosition.BEFORE){
            sb.append(value.valueWithAnnotatedPrefix)
        } else {
            sb.append(value.valueWithAnnotatedPostfix)
        }

        return sb.toString()
    }

    enum class AnnotationPosition{
        BEFORE,
        AFTER,
        NONE;
    }
}