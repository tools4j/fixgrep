package org.tools4j.fix

/**
 * User: ben
 * Date: 12/06/2017
 * Time: 6:43 AM
 */
class FixSpec(
        private val fieldsAndEnumValues: Map<String, String>,
        private val headerFields: Set<String>,
        private val trailerFields: Set<String>,
        private val messageTypes: Map<String, String>) {

    fun getField(tagInt: Number, rawValue: String): Field {
        val tag = getTag("" + tagInt)
        val value = getValue(tag, rawValue)
        return Field(tag, value)
    }

    fun getField(tagStr: String, valueStr: String): Field {
        val tag = getTag(tagStr)
        val value = getValue(tag, valueStr)
        return Field(tag, value)
    }

    fun getMsgType(messageTypeEnum: String): String? {
        return messageTypes[messageTypeEnum];
    }

    private fun getTag(tagStr: String): Tag {
        val tagDescription = this.fieldsAndEnumValues[tagStr]
        val tagNumber: Int?
        try {
            tagNumber = Integer.valueOf(tagStr)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Cannot translate tag $tagStr to a number.  All tags must be numeric.")
        }

        return if (tagDescription != null) {
            SpecTag(tagNumber, tagDescription)
        } else {
            UnknownTag(tagNumber)
        }
    }

    private fun getValue(tag: Tag, rawValue: String): Value {
        val tagEnumKey = tag.tag.toString() + "" + rawValue
        val tagEnumValue = this.fieldsAndEnumValues[tagEnumKey]
        return if (tagEnumValue != null) {
            EnumValue(rawValue, tagEnumValue)
        } else {
            NonEnumValue(rawValue)
        }
    }


    override fun toString(): String {
        return "FixspecProperties{" + "\n" +
                "    fieldsAndEnumValues=" + fieldsAndEnumValues + "\n" +
                "    headerFields=" + headerFields + "\n" +
                "    trailerFields=" + trailerFields + "\n" +
                "    messageTypes=" + messageTypes + "\n" +
                '}'.toString()
    }


}
