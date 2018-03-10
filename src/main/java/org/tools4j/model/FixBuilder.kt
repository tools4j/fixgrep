package org.tools4j.model

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:42 PM
 */
class FixBuilder(private val delimiter: String) {
    private val sb = StringBuilder()

    fun with(tag: Int, value: Double): FixBuilder {
        return with(tag, "" + value)
    }

    fun with(tag: Int, value: Long): FixBuilder {
        return with(tag, "" + value)
    }

    fun with(tag: Int, value: Boolean): FixBuilder {
        return with(tag, "" + value)
    }

    fun with(tag: Int, value: String): FixBuilder {
        if (sb.length > 0) {
            sb.append(delimiter)
        }
        sb.append(tag).append("=").append(value)
        return this
    }

    override fun toString(): String {
        return sb.toString()
    }

    fun withIfNotNull(tag: Int, value: Double?): FixBuilder {
        return if (value != null) with(tag, value) else this
    }

    fun withIfNotNull(tag: Int, value: Long?): FixBuilder {
        return if (value != null) with(tag, value) else this
    }

    fun withIfNotNull(tag: Int, value: String?): FixBuilder {
        return if (value != null) with(tag, value) else this
    }

    fun withIfNotNull(tag: Int, value: Boolean?): FixBuilder {
        return if (value != null) with(tag, value) else this
    }
}
