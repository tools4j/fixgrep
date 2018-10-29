package org.tools4j.fixgrep.formatting

import org.tools4j.extensions.constantToCapitalCase
import org.tools4j.fix.*
import org.tools4j.fixgrep.linehandlers.FixLine
import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.utils.Constants.Companion.DOLLAR

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class Formatter (val spec: FormatSpec){

    fun format(line: FixLine): String? {
        var formattedString = spec.getOutputFormat()
        if (formattedString.contains("\${msgTypeName}")) {
            val msgTypeName = msgTypeAndExecTypeName(line.fields)
            formattedString = formattedString.replace("\${msgTypeName}", msgTypeName)
        }

        if (formattedString.contains("\${msgColor}")) {
            val replaceWith: String
            if(!spec.suppressColors) {
                val msgColor = spec.msgColors.getColor(line.fields)
                if(spec.formatInHtml){
                    replaceWith = "<span class='${msgColor.htmlClass}'>"
                } else {
                    replaceWith = msgColor.consoleTextBefore
                }
            } else {
                replaceWith = ""
            }
            formattedString = formattedString.replace("\${msgColor}", replaceWith)
        }

        if (formattedString.contains("\${colorReset}")) {
            val replaceWith: String
            if(!spec.suppressColors) {
                if(spec.formatInHtml){
                    replaceWith = "</span>"
                } else {
                    replaceWith = Ansi.Reset
                }
            } else {
                replaceWith = ""
            }
            formattedString = formattedString.replace("\${colorReset}", replaceWith)
        }

        if (formattedString.contains("\${msgTypeName}")) {
            val msgTypeCode = line.fields.msgTypeCode
            val msgTypeName: String
            if (msgTypeCode == "8") {
                val execTypeCode = line.fields.getField(150)!!.value.valueRaw
                val execTypeString = spec.fixSpec.fieldsByNumber[150]!!.enumsByCode[execTypeCode]
                val execTypeStringAsCapitalCase = execTypeString!!.constantToCapitalCase()
                msgTypeName = "Exec." + execTypeStringAsCapitalCase
            } else {
                msgTypeName = spec.fixSpec.messagesByMsgType[msgTypeCode]!!.name
            }
            formattedString = formattedString.replace("\${msgTypeName}", msgTypeName)
        }

        if (formattedString.contains("\${senderToTargetCompIdDirection}")) {
            val senderCompId: String? = line.fields.getField(FixFieldTypes.SenderCompID)?.stringValue()
            val targetCompId: String? = line.fields.getField(FixFieldTypes.TargetCompID)?.stringValue()

            val direction: String
            if (senderCompId != null && targetCompId != null) {
                direction = "${senderCompId}->${targetCompId}"
            } else {
                direction = ""
            }
            formattedString = formattedString.replace("\${senderToTargetCompIdDirection}", direction)
        }

        if (formattedString.contains("\${msgFix}")) {
            var fields = FieldsAnnotator(line.fields, spec.fixSpec).fields
            fields = fields.sortBy(spec.sortByTags)
            if(!spec.suppressColors) fields = spec.highlight.apply(fields)
            val formattedFix = spec.getMsgFormatter(fields).format()
            formattedString = formattedString.replace("\${msgFix}", formattedFix)
        }

        for (i in 1..line.matcher.groupCount()) {
            if (formattedString.contains("$" + i)) {
                val groupValue = line.matcher.group(i)
                if (groupValue != null) {
                    formattedString = formattedString.replace("${DOLLAR}$i", groupValue)
                } else {
                    formattedString = formattedString.replace("$" + i + " ", "")
                    formattedString = formattedString.replace("$" + i, "")
                }
            }
        }

        if(formattedString.isEmpty()){
            return null
        } else if(spec.debug){
            return formattedString.replace("\u001b", "\\u001b")
        } else {
            return formattedString
        }
    }

    fun msgTypeAndExecTypeName(fields: Fields): String {
        if(fields.msgTypeCode != "8"){
            val msg = spec.fixSpec.messagesByMsgType.get(fields.msgTypeCode)
            if(msg == null) {
                FixSpec.logger.debug { "Could not find message type for msgTypeCode: $fields.msgTypeCode returning 'Unknown'" }
                return "Unknown"
            } else {
                return msg.name
            }
        } else if(fields.getField(150) != null){
            val execTypeCode = fields.getField(150)?.value?.valueRaw
            val execTypeName = if(execTypeCode != null && ExecType.codeExists(execTypeCode)) ExecType.forCode(execTypeCode) else execTypeCode
            return "Exec." + execTypeName
        } else {
            return "ExecutionReport"
        }
    }
}