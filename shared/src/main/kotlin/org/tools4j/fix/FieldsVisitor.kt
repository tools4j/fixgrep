package org.tools4j.fix

/**
 * User: benjw
 * Date: 7/16/2018
 * Time: 5:06 PM
 */
interface FieldsVisitor {
    fun visit(fields: Fields)
    fun getFieldVisitor(): FieldVisitor
}