package org.tools4j.model

/**
 * User: ben
 * Date: 27/10/2016
 * Time: 5:49 PM
 */
open class SimpleId(override val id: String) : Id {

    constructor(id: Int) : this(""+id)
    constructor(id: Long) : this(""+id)

    override fun toString(): String {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleId) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
