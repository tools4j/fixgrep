package org.tools4j.fix

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
        val OUTSIDE_ANNOTATED_STR = "outsideAnnotated"
        val INSIDE_ANNOTATED_STR = "insideAnnotated"
        val REPLACED_STR = "replaced"
        val NONE_STR = "none"

        init{
            specs[OUTSIDE_ANNOTATED_STR] = AnnotationPositions(BEFORE, AFTER)
            specs[INSIDE_ANNOTATED_STR] = AnnotationPositions(AFTER, BEFORE)
            specs[REPLACED_STR] = AnnotationPositions(REPLACE, REPLACE)
            for(firstSymbol in AnnotationPosition.values()){
                for(secondSymbol in AnnotationPosition.values()){
                    specs["${firstSymbol.abbrev}${secondSymbol.abbrev}"] = AnnotationPositions(firstSymbol, secondSymbol)
                }
                specs["${firstSymbol.abbrev}"] = AnnotationPositions(firstSymbol, firstSymbol)
            }
            specs[NONE_STR] = NO_ANNOTATION
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