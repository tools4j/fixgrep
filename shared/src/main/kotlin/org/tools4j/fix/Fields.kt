package org.tools4j.fix

/**
 * User: ben
 * Date: 15/03/2018
 * Time: 6:47 AM
 */
interface Fields: MutableList<Field>{
    fun countOfField(tag: Tag): Int
    fun getField(tag: Tag): Field?
    fun countOfField(tag: Int): Int
    fun getField(tag: Int): Field?
    fun exists(tag: Tag): Boolean
    fun exists(tag: Int): Boolean
    fun sortBy(desiredOrder: List<Int>): Fields
    fun toIntToStringMap(): Map<Int, String>
    fun toDelimitedString(delimiter: Char = '|'): String
    fun exclude(excludeTags: List<Int>): Fields
    fun includeOnly(onlyIncludeTags: List<Int>): Fields
    val pipeDelimitedString: String
    val msgTypeCode: String
    val msgTypeAndExecTypeKey: String
    val outputDelimiter: Delimiter
    fun toHtml(): String
    fun toConsoleText(): String;
    fun hasRepeatingTags(): Boolean

    fun toDelimitedString(delimiter: String): String {
        if(delimiter.length != 1) throw IllegalArgumentException("Delimiter must be one character long: [$delimiter]")
        return toDelimitedString(delimiter.toCharArray()[0])
    }

    companion object {
        fun getMsgTypeAndExecTypeKey(msgType: String, execType: String?): String{
            return if(execType != null) (msgType + "." + execType) else msgType
        }

        fun getMsgTypeAndExecTypeKey(msgType: FixMessageType, execType: ExecType?): String{
            return if(execType != null) (msgType.code + "." + execType.code) else msgType.code
        }
    }
}