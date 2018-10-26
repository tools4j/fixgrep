package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPosition

/**
 * User: benjw
 * Date: 11/10/2018
 * Time: 06:45
 */
abstract class AbstractPartAppender(val annotationPosition: AnnotationPosition) {
    fun append(sb: StringBuilder) {
        when (annotationPosition) {
            AnnotationPosition.NONE -> {
                appendRaw(sb)
            }
            AnnotationPosition.BEFORE -> {
                if (hasAnnotation()) appendAnnotation(sb)
                appendRaw(sb)
            }
            AnnotationPosition.AFTER -> {
                appendRaw(sb)
                if (hasAnnotation()) appendAnnotation(sb)
            }
            AnnotationPosition.REPLACE -> {
                if (hasAnnotation()) {
                    appendAnnotation(sb)
                } else {
                    appendRaw(sb)
                }
            }
            else -> throw IllegalArgumentException("Unknown position: " + annotationPosition)
        }
    }

    abstract fun hasAnnotation(): Boolean
    abstract fun appendAnnotation(sb: StringBuilder)
    abstract fun appendRaw(sb: StringBuilder)
}