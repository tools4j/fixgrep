package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:37 PM
 */
open class RawTag(override val tag: Int) : Tag {
    constructor(tag: String): this(tag.toInt())

    override fun toHtml(): String {
        return "<span class='tag rawTag'>$tag</span>"
    }

    override fun toConsoleText(): String {
        return ""+tag
    }

    override fun toString(): String {
        return "" + tag
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawTag) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }
}
