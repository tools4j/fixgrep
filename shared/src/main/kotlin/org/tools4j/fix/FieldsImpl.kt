package org.tools4j.fix

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:43 PM
 */
class FieldsImpl(val fields: MutableList<Field>) : ArrayList<Field>(fields), Fields {
    override fun countOfField(tag: Tag): Int {
        return countOfField(tag.tag)
    }

    override fun getField(tag: Tag): Field? {
        return getField(tag.tag)
    }

    override fun countOfField(tag: Int): Int {
        return fields.count { it.tag.tag == tag }
    }

    override fun getField(tag: Int): Field? {
        if(countOfField(tag) > 1) throw IllegalArgumentException("More than one tag [$tag] exists in fields: $fields")
        return fields.firstOrNull { it.tag.tag == tag }
    }

    override fun exists(tag: Tag): Boolean {
        return countOfField(tag) > 0
    }

    override fun exists(tag: Int): Boolean {
        return countOfField(tag) > 0
    }

    override fun remove(tag: Int) {
        fields.removeIf { it.tag.tag == tag }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (field in fields) {
            sb.append(field.withOutsideAnnotations)
            sb.append("|")
        }
        return sb.toString()
    }

    override fun sortBy(desiredOrder: List<Int>): Fields {
        val existingFields = ArrayList(fields)
        val outputFields = ArrayList<Field>()
        for(tag in desiredOrder){
            var foundAtIndex: Int? = null
            for(i in existingFields.indices){
                if(existingFields[i].tag.tag == tag){
                    if(foundAtIndex != null){
                        //We have already found this tag, this means it's probably part of a repeating group.  For now
                        //sorting of members inside a repeating group is not supported.
                        foundAtIndex = null
                        break
                    }
                    foundAtIndex = i
                }
            }
            if(foundAtIndex != null){
                outputFields.add(existingFields.removeAt(foundAtIndex))
            }
        }
        outputFields.addAll(existingFields)
        return FieldsImpl(outputFields)
    }

    override fun toIntToStringMap(): Map<Int, String> {
        return fields.map { it.tag.tag to it.value.toString() }.toMap()
    }

    override fun toDelimitedString(delimiter: Char): String {
        return fields.joinToString(""+delimiter)
    }

    override fun toOutsideAnnotatedString(delimiter: Char): String {
        return OutsideAnnotatedSingleLineFormat(this, delimiter).toString()
    }
}
