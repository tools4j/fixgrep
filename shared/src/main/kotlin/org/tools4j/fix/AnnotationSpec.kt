package org.tools4j.fix

class AnnotationSpec(val annotationPositions: AnnotationPositions, val boldTagAndValue: Boolean){
    companion object {
        val OUTSIDE_ANNOTATED_BOLD_TAG_VALUES: AnnotationSpec = AnnotationSpec(AnnotationPositions.OUTSIDE_ANNOTATED, true)
        val NONE: AnnotationSpec = AnnotationSpec(AnnotationPositions.NO_ANNOTATION, true)
    }
}