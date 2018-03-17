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
    fun remove(tag: Int)
    fun sortBy(desiredOrder: List<Int>): Fields
    fun toIntToStringMap(): Map<Int, String>
    fun toDelimitedString(delimiter: Char = '|'): String
    fun toOutsideAnnotatedString(delimiter: Char = '|'): String
}