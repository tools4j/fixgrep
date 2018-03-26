package org.tools4j.fixgrep

import org.tools4j.extensions.constantToCapitalCase
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixSpec
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:09 AM
 */
class FixMessageTypeLabellingStringProcessor(
        val search: String,
        val replace: String,
        val messageColors: MessageColors,
        val colorize: Boolean,
        output: MessageStringProcessor): AbstractMessageStringProcessingPipe(output) {

    constructor(output: MessageStringProcessor): this(
            "^((\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d+) ?)?",
            "$2 [\$messageType] ",
            output = output,
            messageColors = MessageColors(),
            colorize = true)

    val MESSAGE_TYPE_TOKEN: String = "\$messageType"

    init {
        if(search.contains(MESSAGE_TYPE_TOKEN)){
            throw IllegalArgumentException("Replace string must contain $MESSAGE_TYPE_TOKEN in it somewhere.")
        }
    }

    val fixSpec: FixSpec by lazy {
        Fix50SP2FixSpecFromClassPath().load()
    }

    val searchPattern: Pattern by lazy {
        Pattern.compile(search)
    }

    override fun accept(messageString: MessageString) {
        val msgTypeCode = messageString.originalFields.getField(FixFieldTypes.MsgType)!!.value.rawValue
        val msgTypeName: String
        if(msgTypeCode == "8"){
            val execTypeCode = messageString.originalFields.getField(150)!!.value.rawValue
            val execTypeString = fixSpec.fieldsAndEnumValues.get("150." + execTypeCode)
            val execTypeStringAsCapitalCase = execTypeString!!.constantToCapitalCase()
            msgTypeName = "Exec." + execTypeStringAsCapitalCase
        } else {
            msgTypeName = fixSpec.getMsgTypeNameGivenCode(msgTypeCode)!!
        }
        val replaceStr: String
        if(colorize){
            val ansiCode = messageColors.getColor(messageString.originalFields).ansiCode
            println(ansiCode + "hello" + AnsiColor.Reset.ansiCode)
            replaceStr = ansiCode + msgTypeName + AnsiColor.Reset.ansiCode
        } else {
            replaceStr = msgTypeName
        }
        val replaceStrWithMsgTypeEmbedded = replace.replace(MESSAGE_TYPE_TOKEN, replaceStr)
        val matcher = searchPattern.matcher(messageString.messageText)
        val replacedStr = matcher.replaceFirst(replaceStrWithMsgTypeEmbedded)
        output.accept(messageString.withMessageText(replacedStr))
    }
}