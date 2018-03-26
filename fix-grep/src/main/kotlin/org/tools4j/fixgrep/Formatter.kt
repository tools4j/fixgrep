package org.tools4j.fixgrep

import org.tools4j.extensions.constantToCapitalCase
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsAnnotator
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixSpec
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class Formatter {
    val logLineRegex = "^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d+)?.*?(\\d+=.*$)"
    val logLineRegexGroupContainingMessage = 2
    val format = "$1 \${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFixOutsideAnnotated}"
    val fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().load()
    val msgColors: MessageColors = MessageColors()
    val inputFixDelimiter: Char = '|'
    val outputFixDelimiter: Char = '|'

    /*
    TODO marketToClientIdDirection
     */

    val logLineRegexPattern: Pattern by lazy {
        Pattern.compile(logLineRegex)
    }

    fun format(line: String): String {
        val matcher = logLineRegexPattern.matcher(line)
        if(!matcher.find()){
            return "ERROR: could not match regex with line: ${line}"
        }

        val fixString = matcher.group(logLineRegexGroupContainingMessage)
        val fields: Fields = FieldsFromDelimitedString(fixString, inputFixDelimiter).fields

        var formattedLine = format
        if(formattedLine.contains("\${msgTypeName}")){
            val msgTypeName = fixSpec.messageTypeCodesToNames.get(fields.msgTypeCode)
            formattedLine = formattedLine.replace("\${msgTypeName}", msgTypeName!!)
        }

        if(formattedLine.contains("\${msgColor}")){
            val msgColor = msgColors.getColor(fields)
            formattedLine = formattedLine.replace("\${msgColor}", msgColor.ansiCode)
        }

        if(formattedLine.contains("\${colorReset}")){
            formattedLine = formattedLine.replace("\${colorReset}", AnsiColor.Reset.ansiCode)
        }

        if(formattedLine.contains("\${msgTypeName}")){
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

        if(formattedLine.contains("\${senderToTargetCompIdDirection}")){
            val senderCompId: String? = fields.getField(FixFieldTypes.SenderCompID)?.stringValue()
            val targetCompId: String? = fields.getField(FixFieldTypes.TargetCompID)?.stringValue()

            val direction: String
            if(senderCompId != null && targetCompId != null){
                direction = "${senderCompId}->${targetCompId}"
            } else {
                direction = ""
            }
            formattedLine = formattedLine.replace("\${senderToTargetCompIdDirection}", direction)
        }

        if(formattedLine.contains("\${msgFix")){
            if(formattedLine.contains("\${msgFixOutsideAnnotated}")) {
                val annotatedFields: Fields = FieldsAnnotator(fixSpec, fields).fields
                formattedLine = formattedLine.replace("\${msgFixOutsideAnnotated}", annotatedFields.toOutsideAnnotatedString(outputFixDelimiter))
            } else if(formattedLine.contains("\${msgFix}")){
                formattedLine = formattedLine.replace("\${msgFix}", fields.toDelimitedString(outputFixDelimiter))
            }
        }

        for(i in 1..matcher.groupCount()){
            if(formattedLine.contains("\$${i}")){
                val groupValue = matcher.group(i)
                if(groupValue != null) {
                    formattedLine = formattedLine.replace("\$${i}", groupValue)
                }
            }
        }
        return formattedLine
    }
}