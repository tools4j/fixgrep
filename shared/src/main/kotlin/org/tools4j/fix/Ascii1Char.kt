package org.tools4j.fix

/**
 * User: ben
 * Date: 4/7/17
 * Time: 6:49 PM
 */
class Ascii1Char {
    private val ascii1Char: Char

    init {
        this.ascii1Char = '\u0001'
    }

    fun splitOn(str: String): Array<String>? {
        return SplitableByCharString(str, ascii1Char).split().values()
    }

    override fun toString(): String {
        return "" + ascii1Char
    }

    fun toChar(): Char {
        return ascii1Char
    }
}
