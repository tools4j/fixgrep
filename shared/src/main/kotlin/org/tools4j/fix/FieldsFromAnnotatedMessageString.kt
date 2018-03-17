package org.tools4j.fix

import java.util.ArrayList
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 4/7/17
 * Time: 6:48 AM
 */
class FieldsFromAnnotatedMessageString(
        private val annotatedMessage: String,
        private val delimiterRegex: String) : FieldsSource {

    override val fields: Fields by lazy {
        val fixFields = ArrayList<Field>()
        val ascii1Char = Ascii1Char()

        val parts = ascii1Char.splitOn(StrippedFixString(annotatedMessage, delimiterRegex).toString())
        val partsList = ArrayList<String>()
        for (part in parts!!) {
            val trimmedPart = part.trim { it <= ' ' }
            if (trimmedPart.length != 0) {
                partsList.add(trimmedPart)
            }
        }

        for (part in partsList) {
            val field: Field
            try {
                field = parseField(part)
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot parse field '$part' in expression:$annotatedMessage", e)
            }

            fixFields.add(field)
        }
        FieldsImpl(fixFields)
    }

    fun parseField(fieldStr: String): Field {
        val splitString = SplitableByCharString(fieldStr, '=').split()
        val fieldTypeStr = splitString.values()!![0]
        val fieldType = parseFieldTag(fieldTypeStr)
        val fieldValue = parseFieldValue(splitString.allElementsOnwards(1, "="))
        return Field(fieldType, fieldValue)
    }

    fun parseFieldValue(_fieldValueStr: String?): Value {
        var fieldValueStr = _fieldValueStr

        //Check whether there are escaped square brackets, if so, unescape them, and return literal
        if (fieldValueStr!!.contains("\\[") || fieldValueStr.contains("\\]")) {
            fieldValueStr = fieldValueStr.replace("\\]", "]").replace("\\[", "[")
        }

        //Check whether is in the format enum[desc], e.g: "D[REJECT]"
        var matcher = FIELD_VALUE_WITH_DESC_THEN_ENUM.matcher(fieldValueStr)
        if (matcher.matches()) {
            val parsedDesc = matcher.group(1)
            val parsedEnum = matcher.group(2)
            return EnumValue(parsedEnum, parsedDesc)
        }

        //Check whether is in the format [desc]enum, e.g.: "[REJECT]D"
        matcher = FIELD_VALUE_WITH_ENUM_THEN_DESC.matcher(fieldValueStr)
        if (matcher.matches()) {
            val parsedEnum = matcher.group(1)
            val parsedDesc = matcher.group(2)
            return EnumValue(parsedEnum, parsedDesc)
        }

        //Otherwise, just return the value
        return NonEnumValue(fieldValueStr)
    }

    fun parseFieldTag(fieldTagStr: String): Tag {
        var matcher = TAG_PATTERN_WITH_TEXT_THEN_NUMBER.matcher(fieldTagStr)
        if (matcher.matches()) {
            val tagText = matcher.group(1)
            val tagNumber = matcher.group(2)
            return SpecTag(tagNumber, tagText)
        }

        matcher = TAG_PATTERN_WITH_NUMBER_THEN_TEXT.matcher(fieldTagStr)
        if (matcher.matches()) {
            val tagNumber = matcher.group(1)
            val tagText = matcher.group(2)
            return SpecTag(tagNumber, tagText)
        }

        matcher = TAG_PATTERN_WITH_JUST_NUMBER.matcher(fieldTagStr)
        if (matcher.matches()) {
            val tagNumber = matcher.group(1)
            return UnknownTag(tagNumber)
        }

        throw IllegalArgumentException("Cannot parse field type:" + fieldTagStr)
    }

    companion object {
        val TAG_PATTERN_WITH_TEXT_THEN_NUMBER = Pattern.compile("\\[([^\\]]+)\\](\\d+)")
        val TAG_PATTERN_WITH_NUMBER_THEN_TEXT = Pattern.compile("(\\d+)\\[([^\\]]+)\\]")
        val TAG_PATTERN_WITH_JUST_NUMBER = Pattern.compile("(\\d+)")
        val FIELD_VALUE_WITH_DESC_THEN_ENUM = Pattern.compile("\\[([^\\]]+)\\](.+)")
        val FIELD_VALUE_WITH_ENUM_THEN_DESC = Pattern.compile("(\\w+)\\[([^\\]]+)\\]")
    }
}
