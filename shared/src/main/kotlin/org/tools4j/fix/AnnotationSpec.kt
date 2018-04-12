package org.tools4j.fix

import org.tools4j.fix.AnnotatedField.AnnotationPosition.*
import java.util.stream.Collectors

class AnnotationSpec(val tagAnnotationPosition: AnnotatedField.AnnotationPosition, val valueAnnotationPosition: AnnotatedField.AnnotationPosition){
    fun annotateFields(fields: Fields): Fields {
        return FieldsImpl(fields.stream().map { annotateField(it) }.collect(Collectors.toList()))
    }
    
    fun annotateField(field: Field): Field {
        return if(this == NO_ANNOTATION) field
                   else AnnotatedField(field, this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotationSpec) return false

        if (tagAnnotationPosition != other.tagAnnotationPosition) return false
        if (valueAnnotationPosition != other.valueAnnotationPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagAnnotationPosition.hashCode()
        result = 31 * result + valueAnnotationPosition.hashCode()
        return result
    }

    override fun toString(): String {
        return "AnnotationSpec(tagAnnotationPosition=$tagAnnotationPosition, valueAnnotationPosition=$valueAnnotationPosition)"
    }


    companion object {
        val specs: MutableMap<String, AnnotationSpec> = HashMap()
        val OUTSIDE_ANNOTATED = AnnotationSpec(BEFORE, AFTER)
        val NO_ANNOTATION = AnnotationSpec(NONE, NONE)

        init{
            specs["outsideAnnotated"] = AnnotationSpec(BEFORE, AFTER)
            specs["insideAnnotated"] = AnnotationSpec(AFTER, BEFORE)
            specs["ab"] = AnnotationSpec(AFTER, BEFORE)
            specs["ba"] = AnnotationSpec(BEFORE, AFTER)
            specs["bb"] = AnnotationSpec(BEFORE, BEFORE)
            specs["aa"] = AnnotationSpec(AFTER, AFTER)
            specs["a_"] = AnnotationSpec(AFTER, NONE)
            specs["b_"] = AnnotationSpec(BEFORE, NONE)
            specs["_a"] = AnnotationSpec(NONE, AFTER)
            specs["_b"] = AnnotationSpec(NONE, BEFORE)
            specs["__"] = NO_ANNOTATION
            specs["none"] = NO_ANNOTATION
        }

        @JvmStatic
        fun parse(spec: String): AnnotationSpec {
            var returnSpec = specs.get(spec)
            if(returnSpec == null){
                throw IllegalArgumentException("Unknown spec [" + spec + "] legal specs are: " + specs.keys)
            }
            return returnSpec
        }
    }
}