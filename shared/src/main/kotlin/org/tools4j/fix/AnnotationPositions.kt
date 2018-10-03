package org.tools4j.fix

import java.util.stream.Collectors
import org.tools4j.fix.AnnotationPosition.*

class AnnotationPositions(val tagAnnotationPosition: AnnotationPosition, val valueAnnotationPosition: AnnotationPosition){
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
        val LEFT_ANNOTATED = AnnotationPositions(BEFORE, BEFORE)
        val REPLACED = AnnotationPositions(REPLACE, REPLACE)
        val NO_ANNOTATION = AnnotationPositions(NONE, NONE)

        init{
            specs["outsideAnnotated"] = AnnotationPositions(BEFORE, AFTER)
            specs["insideAnnotated"] = AnnotationPositions(AFTER, BEFORE)
            specs["replaced"] = AnnotationPositions(REPLACE, REPLACE)
            for(tagAnnotation in AnnotationPosition.values()){
                for(valueAnnotation in AnnotationPosition.values()){
                    specs["${tagAnnotation.abbrev}${valueAnnotation.abbrev}"] = AnnotationPositions(tagAnnotation, valueAnnotation)
                }
            }
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