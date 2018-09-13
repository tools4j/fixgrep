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
abstract class FieldFormatter(val context: FormattingContext): FieldVisitor, ValueVisitor, TagVisitor{

    var tagRaw: Int? = null
    var valueRaw: String? = null

    var tagAnnotation: String? = null
    var valueAnnotation: String? = null

    var fieldTextEffect: TextEffect = TextEffect.NONE
        set(value) {
            field = CompositeTextEffect(field, value)
        }

    private val doFirsts = ArrayList<() -> (Any)>()
    private val doLasts = ArrayList<() -> (Any)>()

    override fun visit(field: Field) {
        //TEXT EFFECTS
        if(field is HighlightedField){
            fieldTextEffect = field.textEffect
        }

        //FIELD DETAILS
        field.tag.accept(this)
        field.value.accept(this)

        detectGroupPositioning(field)

        doFirsts.forEach { it.invoke() }
        onFieldBody()
        doLasts.forEach { it.invoke() }
        finish()
    }

    private fun detectGroupPositioning(field: Field) {
        if(context.groupStack.getCurrentMessageOrGroupContext() == null) return
        val fieldSpec = context.fixSpec.fieldsByNumber[field.tag.number]

        //Check whether we are exiting group(s)
        while (!context.groupStack.getCurrentMessageOrGroupContext()!!.fields.contains(fieldSpec)) {
            //Check whether we are entering a new group repeat
            val enteringNewGroupAfterGroupExit = context.groupStack.getCurrentMessageOrGroupContext()?.getGroupByLeadingFieldNumber(field.tag.number)
            if (enteringNewGroupAfterGroupExit != null) {
                context.groupStack.push(enteringNewGroupAfterGroupExit)
                onGroupEnter()
                return
            }

            //Check whether we are exiting a group
            if(!context.groupStack.empty() && fieldSpec != null && !context.groupStack.peek().fields.contains(fieldSpec)) {
                val repeatNumberBeforeExitingGroup = context.groupStack.getCurrentRepeatNumber()
                context.groupStack.pop()
                if (repeatNumberBeforeExitingGroup > 0) onGroupRepeatExit()
                onGroupExit()
            } else {
                break
            }
        }

        //Check whether we are entering a new repeat
        if (!context.groupStack.empty() && field.tag.number == context.groupStack.peek().firstField?.number) {
            val newRepeatNumber = context.groupStack.incrementRepeatNumber()
            if (newRepeatNumber > 1) onGroupRepeatExit()
            onGroupRepeatEnter()

        //Otherwise, assume this is a subsequent field in the current repeat
        } else if(!context.groupStack.empty()){
            onSubsequentFieldInGroupRepeat()
        }
    }

    abstract fun finish()

    fun doFirst(task: () -> Any){
        doFirsts.add(task)
    }

    fun doLast(task: () -> Any){
        doLasts.add(task)
    }

    open fun onGroupRepeatExit() {
        //no-op
    }

    open fun onGroupExit() {
        //no-op
    }

    open fun onGroupRepeatEnter() {
        //no-op
    }

    open fun onSubsequentFieldInGroupRepeat(){
        //no-op
    }

    open fun onGroupEnter() {
        //no-op
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

    open fun onFieldBody(){
        //no-op
    }
}