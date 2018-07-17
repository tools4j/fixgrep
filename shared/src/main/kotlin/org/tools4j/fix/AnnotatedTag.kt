package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:36 PM
 */
class AnnotatedTag(override val tagRaw: Int, val annotation: String) : Tag {
    constructor(tag: String, description: String): this(Integer.parseInt(tag), description)

    override fun accept(tagVisitor: TagVisitor) {
        tagVisitor.visit(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotatedTag) return false

        if (tagRaw != other.tagRaw) return false
        if (annotation != other.annotation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagRaw
        result = 31 * result + annotation.hashCode()
        return result
    }
}
