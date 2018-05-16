package org.tools4j.fix

/**
 * User: ben
 * Date: 11/04/2018
 * Time: 6:12 PM
 */
class AnnotatedFields(sourceFields: Fields, annotationSpec: AnnotationSpec, fixSpec: FixSpec): FieldsSource {
    constructor(sourceFields: Fields, annotationSpec: String, fixSpec: FixSpec, boldTagAndValue: Boolean): this(sourceFields, AnnotationSpec(AnnotationPositions.parse(annotationSpec), boldTagAndValue), fixSpec)

    override val fields: Fields by lazy {
        FieldsAnnotator(sourceFields, fixSpec, annotationSpec).fields
    }
}