package org.tools4j.fixgrep.formatting

import org.tools4j.extensions.padEnd
import org.tools4j.extensions.padStart
import org.tools4j.fix.Delimiter
import org.tools4j.fix.DelimiterImpl
import org.tools4j.fix.FieldVisitor

/**
 * User: benjw
 * Date: 7/12/2018
 * Time: 6:25 AM
 */
class VerticalAlignedConsoleMsgFormatter(val context: FormattingContext) : MsgFormatter() {
    val fieldDetailsList = ArrayList<FieldDetails>()
    var atLeastOneFieldWritten = false

    override fun getFieldVisitor(): FieldVisitor {
        return VerticalAlignedConsoleFieldFormatter(this, context, msgTextEffect)
    }

    fun writeFieldDetails(fieldDetails: FieldDetails) {
        fieldDetailsList.add(fieldDetails)
    }

    override fun format(): String{
        context.fields.accept(this)

        val maxTagWidth: Int = fieldDetailsList.map { it.tagWithoutTextEffects.length }.max()!!
        val maxEqualsWidth: Int = fieldDetailsList.map { it.equalsWithoutTextEffects.length }.max()!!
        val maxValueWidth: Int = fieldDetailsList.map { it.valueWithoutTextEffects.length }.max()!!

        val sb = StringBuilder()
        for(fieldDetails in fieldDetailsList){
            sb.append(fieldDetails.textEffectsBeforeField)
            sb.append(fieldDetails.tagWithTextEffects.padStart(maxTagWidth + 1, fieldDetails.tagWithoutTextEffects.length, ' '))
            sb.append(fieldDetails.equalsWithTextEffects.padStart(maxEqualsWidth + 1, fieldDetails.equalsWithoutTextEffects.length, ' ') + " ")
            sb.append(fieldDetails.valueWithTextEffects.padEnd(maxValueWidth + 1, fieldDetails.valueWithoutTextEffects.length, ' '))
            sb.append(fieldDetails.textEffectsAfterField)
            sb.append("\n")
        }
        sb.append("\n")
        return sb.toString()
    }

    class FieldDetails(
        val tagWithoutTextEffects: String,
        val equalsWithoutTextEffects: String,
        val valueWithoutTextEffects: String,

        val tagWithTextEffects: String,
        val equalsWithTextEffects: String,
        val valueWithTextEffects: String,

        val textEffectsBeforeField: String,
        val textEffectsAfterField: String
    )
}