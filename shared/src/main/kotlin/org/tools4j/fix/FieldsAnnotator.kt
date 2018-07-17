package org.tools4j.fix

import java.util.ArrayList

/**
 * User: ben
 * Date: 11/7/17
 * Time: 5:19 PM
 */
class FieldsAnnotator(
        val inputFields: Fields,
        val fixSpec: FixSpec,
        val annotationSpec: AnnotationSpec = AnnotationSpec.OUTSIDE_ANNOTATED_BOLD_TAG_VALUES) : FieldsSource {

    val fieldAnnotator = FieldAnnotator(fixSpec, annotationSpec)

    override val fields: Fields by lazy {
        val returnFields = ArrayList<Field>()
        for (field in this.inputFields) {
            returnFields.add(fieldAnnotator.getField(field))
        }
        FieldsImpl(returnFields)
    }
}
