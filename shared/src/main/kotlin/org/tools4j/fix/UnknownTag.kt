package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:37 PM
 */
class UnknownTag(override val tag: Int) : Tag {

    override val tagWithAnnotatedPrefix: String
        get() = "" + tag

    override val tagWithAnnotatedPostfix: String
        get() = "" + tag

    override fun toString(): String {
        return "" + tag
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnknownTag) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }

    constructor(tag: String) : this(Integer.valueOf(tag))
}
