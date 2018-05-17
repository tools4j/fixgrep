package org.tools4j.fixgrep

import org.tools4j.extensions.constantToCapitalCase
import org.tools4j.fix.AnnotatedFields
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FieldsAnnotator
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fixgrep.texteffect.Ansi
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class Formatter(val spec: FormatSpec){
    val logLineRegexPattern: Pattern by lazy {
        Pattern.compile(spec.lineRegex)
    }

    fun shouldParse(line: String): Matcher{
        return logLineRegexPattern.matcher(line)
    }

    fun format(line: String): String? {
        val matcher = shouldParse(line)
        if(!matcher.find()){
            return "ERROR: could not match regex with line: ${line}"
        }
        return format(matcher)
    }

    fun format(matcher: Matcher): String? {
        val fixString = matcher.group(spec.lineRegexGroupForFix)
        val fields: Fields = FieldsFromDelimitedString(fixString, spec.inputDelimiter).fields

        if(!shouldPrint(fields)){
            return null
        }
        return format(fields, matcher)
    }

    private fun shouldPrint(fields: Fields): Boolean {
        if(!spec.includeOnlyMessagesOfType.isEmpty()
            && !spec.includeOnlyMessagesOfType.contains(fields.msgTypeCode)){
            return false
        } else if(!spec.excludeMessagesOfType.isEmpty()
                && spec.excludeMessagesOfType.contains(fields.msgTypeCode)) {
            return false
        } else {
            return true
        }
    }

    fun format(inputFields: Fields, matcher: Matcher): String? {
        var formattedString = spec.lineFormat
        if (formattedString.contains("\${msgTypeName}")) {
            val msgTypeName = spec.fixSpec.msgTypeAndExecTypeName(inputFields)
            formattedString = formattedString.replace("\${msgTypeName}", msgTypeName)
        }

        if (formattedString.contains("\${msgColor}")) {
            val replaceWith: String
            if(!spec.suppressColors) {
                val msgColor = spec.msgColors.getColor(inputFields)
                replaceWith = msgColor.ansiCode
            } else {
                replaceWith = ""
            }
            formattedString = formattedString.replace("\${msgColor}", replaceWith)
        }

        if (formattedString.contains("\${colorReset}")) {
            val replaceWith: String
            if(!spec.suppressColors) {
                replaceWith = Ansi.Reset.ansiCode
            } else {
                replaceWith = ""
            }
            formattedString = formattedString.replace("\${colorReset}", replaceWith)
        }

        if (formattedString.contains("\${msgTypeName}")) {
            val msgTypeCode = inputFields.msgTypeCode
            val msgTypeName: String
            if (msgTypeCode == "8") {
                val execTypeCode = inputFields.getField(150)!!.value.rawValue
                val execTypeString = spec.fixSpec.fieldsAndEnumValues.get("150." + execTypeCode)
                val execTypeStringAsCapitalCase = execTypeString!!.constantToCapitalCase()
                msgTypeName = "Exec." + execTypeStringAsCapitalCase
            } else {
                msgTypeName = spec.fixSpec.getMsgTypeNameGivenCode(msgTypeCode)!!
            }
            formattedString = formattedString.replace("\${msgTypeName}", msgTypeName)
        }

        if (formattedString.contains("\${senderToTargetCompIdDirection}")) {
            val senderCompId: String? = inputFields.getField(FixFieldTypes.SenderCompID)?.stringValue()
            val targetCompId: String? = inputFields.getField(FixFieldTypes.TargetCompID)?.stringValue()

            val direction: String
            if (senderCompId != null && targetCompId != null) {
                direction = "${senderCompId}->${targetCompId}"
            } else {
                direction = ""
            }
            formattedString = formattedString.replace("\${senderToTargetCompIdDirection}", direction)
        }

        if (formattedString.contains("\${msgFix}")) {
            var fields = FieldsAnnotator(inputFields, spec.fixSpec, spec.tagAnnotationSpec).fields
            fields = fields.sortBy(spec.sortByTags)
            fields = fields.exclude(spec.excludeTags)
            fields = fields.includeOnly(spec.onlyIncludeTags)
            if(!spec.suppressColors) fields = spec.highlight.apply(fields)
            val formattedFix = if(spec.formatInHtml) fields.toHtml() else fields.toConsoleText(spec.outputDelimiter)
            formattedString = formattedString.replace("\${msgFix}", formattedFix)
        }

        for (i in 1..matcher.groupCount()) {
            if (formattedString.contains("\$${i}")) {
                val groupValue = matcher.group(i)
                if (groupValue != null) {
                    formattedString = formattedString.replace("\$${i}", groupValue)
                }
            }
        }
        return if(formattedString.isEmpty()) null else formattedString
    }
}