package org.tools4j.fix

import org.tools4j.fix.AnnotatedField.AnnotationPosition.*
import org.tools4j.fix.AnnotationPositions.Companion.NO_ANNOTATION
import java.util.stream.Collectors

class AnnotationSpec(val annotationPositions: AnnotationPositions, val boldTagAndValue: Boolean){
    fun annotateFields(fields: Fields): Fields {
        return FieldsImpl(fields.stream().map { annotateField(it) }.collect(Collectors.toList()))
    }
    
    fun annotateField(field: Field): Field {
        return if(annotationPositions == NO_ANNOTATION) field
                   else AnnotatedField(field, this)
    }
}