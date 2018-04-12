package org.tools4j.fixgrep

import org.tools4j.extensions.constantToCapitalCase
import org.tools4j.fix.AnnotationSpec
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsNameAndEnumEnricher
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixSpec
import org.tools4j.fixgrep.texteffect.Ansi
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class Formatter(
    val logLineRegex: String = "^.*?(\\d+=.*?$)",
    val logLineRegexGroupContainingMessage: Int = 1,
    val format: String = "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
    val fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().load(),
    val msgColors: MessageColors = MessageColors(),
    val inputFixDelimiter: Char = '|',
    val outputFixDelimiter: Char = '|',
    val annotationSpec: AnnotationSpec = AnnotationSpec.OUTSIDE_ANNOTATED){

    val logLineRegexPattern: Pattern by lazy {
        Pattern.compile(logLineRegex)
    }

    fun matches(line: String): Matcher{
        return logLineRegexPattern.matcher(line)
    }

    fun format(line: String): String {
        val matcher = matches(line)
        if(!matcher.find()){
            return "ERROR: could not match regex with line: ${line}"
        }
        return format(matcher)
    }

    fun format(matcher: Matcher): String {
        val fixString = matcher.group(logLineRegexGroupContainingMessage)
        val fields: Fields = FieldsFromDelimitedString(fixString, inputFixDelimiter).fields

        var formattedLine = format
        if (formattedLine.contains("\${msgTypeName}")) {
            val msgTypeName = fixSpec.msgTypeAndExecTypeName(fields)
            formattedLine = formattedLine.replace("\${msgTypeName}", msgTypeName!!)
        }

        if (formattedLine.contains("\${msgColor}")) {
            val msgColor = msgColors.getColor(fields)
            formattedLine = formattedLine.replace("\${msgColor}", msgColor.ansiCode)
        }

        if (formattedLine.contains("\${colorReset}")) {
            formattedLine = formattedLine.replace("\${colorReset}", Ansi.Reset.ansiCode)
        }

        if (formattedLine.contains("\${msgTypeName}")) {
            val msgTypeCode = fields.msgTypeCode
            val msgTypeName: String
            if (msgTypeCode == "8") {
                val execTypeCode = fields.getField(150)!!.value.rawValue
                val execTypeString = fixSpec.fieldsAndEnumValues.get("150." + execTypeCode)
                val execTypeStringAsCapitalCase = execTypeString!!.constantToCapitalCase()
                msgTypeName = "Exec." + execTypeStringAsCapitalCase
            } else {
                msgTypeName = fixSpec.getMsgTypeNameGivenCode(msgTypeCode)!!
            }
            formattedLine = formattedLine.replace("\${msgTypeName}", msgTypeName)
        }

        if (formattedLine.contains("\${senderToTargetCompIdDirection}")) {
            val senderCompId: String? = fields.getField(FixFieldTypes.SenderCompID)?.stringValue()
            val targetCompId: String? = fields.getField(FixFieldTypes.TargetCompID)?.stringValue()

            val direction: String
            if (senderCompId != null && targetCompId != null) {
                direction = "${senderCompId}->${targetCompId}"
            } else {
                direction = ""
            }
            formattedLine = formattedLine.replace("\${senderToTargetCompIdDirection}", direction)
        }

        if (formattedLine.contains("\${msgFix}")) {
            var annotatedFields = FieldsNameAndEnumEnricher(fixSpec, fields).fields
            annotatedFields = annotationSpec.annotateFields(annotatedFields)
            formattedLine = formattedLine.replace("\${msgFix}", annotatedFields.toPrettyString(outputFixDelimiter))
        }

        for (i in 1..matcher.groupCount()) {
            if (formattedLine.contains("\$${i}")) {
                val groupValue = matcher.group(i)
                if (groupValue != null) {
                    formattedLine = formattedLine.replace("\$${i}", groupValue)
                }
            }
        }
        return formattedLine
    }
}