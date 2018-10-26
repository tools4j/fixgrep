package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:37 PM
 */
open class RawTag(override val number: Int) : Tag {
    constructor(tag: String): this(tag.toInt())

    override fun accept(tagVisitor: TagVisitor) {
        tagVisitor.visit(this)
    }

    override fun toString(): String {
        return "" + number
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawTag) return false

        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        return number.hashCode()
    }
}
