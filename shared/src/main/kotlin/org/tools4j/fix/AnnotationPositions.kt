package org.tools4j.fix

import org.tools4j.fix.AnnotatedField.AnnotationPosition.*
import java.util.stream.Collectors

class AnnotationPositions(val tagAnnotationPosition: AnnotatedField.AnnotationPosition, val valueAnnotationPosition: AnnotatedField.AnnotationPosition){
    val neitherTagNorValueAnnotated: Boolean by lazy {
        tagAnnotationPosition == NONE && valueAnnotationPosition == NONE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotationPositions) return false

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
        return "AnnotationPositions(tagAnnotationPosition=$tagAnnotationPosition, valueAnnotationPosition=$valueAnnotationPosition)"
    }

    companion object {
        val specs: MutableMap<String, AnnotationPositions> = HashMap()
        val OUTSIDE_ANNOTATED = AnnotationPositions(BEFORE, AFTER)
        val NO_ANNOTATION = AnnotationPositions(NONE, NONE)

        init{
            specs["outsideAnnotated"] = AnnotationPositions(BEFORE, AFTER)
            specs["insideAnnotated"] = AnnotationPositions(AFTER, BEFORE)
            specs["ab"] = AnnotationPositions(AFTER, BEFORE)
            specs["ba"] = AnnotationPositions(BEFORE, AFTER)
            specs["bb"] = AnnotationPositions(BEFORE, BEFORE)
            specs["aa"] = AnnotationPositions(AFTER, AFTER)
            specs["a_"] = AnnotationPositions(AFTER, NONE)
            specs["b_"] = AnnotationPositions(BEFORE, NONE)
            specs["_a"] = AnnotationPositions(NONE, AFTER)
            specs["_b"] = AnnotationPositions(NONE, BEFORE)
            specs["__"] = NO_ANNOTATION
            specs["none"] = NO_ANNOTATION
        }

        @JvmStatic
        fun parse(spec: String): AnnotationPositions {
            val returnSpec = specs.get(spec)
            if(returnSpec == null){
                throw IllegalArgumentException("Unknown spec [" + spec + "] legal specs are: " + specs.keys)
            }
            return returnSpec
        }
    }
}