package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*
import org.tools4j.fixgrep.highlights.HighlightedField
import org.tools4j.fixgrep.texteffect.CompositeTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:22 AM
 */
abstract class FieldFormatter(): FieldVisitor, ValueVisitor, TagVisitor{

    var tagRaw: Int? = null
    var valueRaw: String? = null

    var tagAnnotation: String? = null
    var valueAnnotation: String? = null

    var fieldTextEffect: TextEffect = TextEffect.NONE
        set(value) {
            field = CompositeTextEffect(field, value)
        }

    override fun visit(field: Field) {
        if(field is HighlightedField){
            fieldTextEffect = field.textEffect
        }
        field.tag.accept(this)
        field.value.accept(this)
        finish()
    }

    override fun visit(tag: Tag) {
        tagRaw = tag.tagRaw
        if(tag is AnnotatedTag){
            tagAnnotation = tag.annotation
        }
    }

    override fun visit(value: Value) {
        valueRaw = value.valueRaw
        if(value is AnnotatedValue){
            valueAnnotation = value.annotation
        }
    }

    abstract fun finish();
}