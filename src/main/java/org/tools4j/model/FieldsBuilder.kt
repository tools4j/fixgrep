package org.tools4j.model

import org.tools4j.fix.Field
import org.tools4j.fix.Fields

import java.util.ArrayList
import java.util.Collections

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:42 PM
 */
class FieldsBuilder : Fields {
    private val fieldsList: MutableList<Field>

    init {
        fieldsList = ArrayList()
    }

    fun with(tag: Int, value: Double?): FieldsBuilder {
        if (value != null) {
            fieldsList.add(Field(tag, ""+value))
            return this
        } else {
            return this
        }
    }

    fun with(tag: Int, value: Long?): FieldsBuilder {
        if (value != null) {
            fieldsList.add(Field(tag, ""+value))
            return this
        } else {
            return this
        }
    }

    fun with(tag: Int, value: Boolean?): FieldsBuilder {
        if (value != null) {
            fieldsList.add(Field(tag, ""+value))
            return this
        } else {
            return this
        }
    }

    fun with(tag: Int, value: Int?): FieldsBuilder {
        if (value != null) {
            fieldsList.add(Field(tag, ""+value))
            return this
        } else {
            return this
        }
    }

    fun with(tag: Int, value: String?): FieldsBuilder {
        if (value != null) {
            fieldsList.add(Field(tag, value))
            return this
        } else {
            return this
        }
    }

    fun with(tag: Int, value: OrdStatus?): FieldsBuilder {
        return if (value != null) {
            with(tag, "" + value.code)
        } else {
            this
        }
    }

    fun with(tag: Int, value: OrderType?): FieldsBuilder {
        return if (value != null) {
            with(tag, "" + value.code)
        } else {
            this
        }
    }

    fun with(tag: Int, value: Id?): FieldsBuilder {
        return if (value != null) {
            with(tag, "" + value)
        } else {
            this
        }
    }

    fun with(tag: Int, value: Side?): FieldsBuilder {
        return if (value != null) {
            with(tag, "" + value.code)
        } else {
            this
        }
    }

    fun with(tag: Int, value: Price?): FieldsBuilder {
        return if (value != null) {
            with(tag, value.toString())
        } else {
            this
        }
    }


    override val fields: List<Field>
        get() = Collections.unmodifiableList(fieldsList)

}
