package org.tools4j.fixgrep.formatting

import org.tools4j.fix.*
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.GroupSpec
import org.tools4j.fix.spec.MessageSpec
import org.tools4j.fixgrep.highlights.HighlightedField
import org.tools4j.fixgrep.texteffect.CompositeTextEffect
import org.tools4j.fixgrep.texteffect.TextEffect

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:22 AM
 */
abstract class FieldFormatter(val context: FormattingContext): FieldVisitor, ValueVisitor, TagVisitor{

    var tagRaw: Int? = null
    var valueRaw: String? = null

    var tagAnnotation: String? = null
    var valueAnnotation: String? = null

    var fieldTextEffect: TextEffect = TextEffect.NONE
        set(value) {
            field = CompositeTextEffect(field, value)
        }

    var currentGroupAndRepeatNumber: GroupSpec? = null
    var groupLevel: Int = 0

    override fun visit(field: Field) {
        val specField = context.fixSpec.fieldsByNumber[field.tag.number]

        //TEXT EFFECTS
        if(field is HighlightedField){
            fieldTextEffect = field.textEffect
        }

        //GROUPS
        //Check whether we are entering a new repeating group type
        val enteringNewGroup = context.messageSpec?.getGroupByLeadingFieldNumber(field.tag.number)
        if(enteringNewGroup != null){
            context.groupStack.push(enteringNewGroup)

        } else if(!context.groupStack.empty()){
            //Check whether we are exiting group(s)
            while(!context.groupStack.empty() && !context.groupStack.peek().fields.contains(specField)){
                context.groupStack.pop()
            }
            //Check whether we are entering a new repeat
            if(!context.groupStack.empty() && field.tag.number == context.groupStack.peek().firstField?.number) {
                context.groupStack.incrementRepeatNumber()
            }
        }
        currentGroupAndRepeatNumber = if(context.groupStack.empty()) null else context.groupStack.peek()
        groupLevel = context.groupStack.size()

        //FIELD DETAILS
        field.tag.accept(this)
        field.value.accept(this)
        finish()
    }

    override fun visit(tag: Tag) {
        tagRaw = tag.number
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