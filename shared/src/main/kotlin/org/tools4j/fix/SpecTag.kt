package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:36 PM
 */
class SpecTag(
        override val tag: Int,
        val description: String) : Tag {

    override val tagWithAnnotatedPrefix: String
        get() = "[$description]$tag"

    override val tagWithAnnotatedPostfix: String
        get() = tag.toString() + "[" + description + "]"

    override fun toString(): String {
        return tagWithAnnotatedPrefix
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SpecTag) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }

    constructor(tag: String, description: String): this(Integer.parseInt(tag), description)
}
