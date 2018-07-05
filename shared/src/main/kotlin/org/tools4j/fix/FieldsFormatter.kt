package org.tools4j.fix

/**
 * User: benjw
 * Date: 7/5/2018
 * Time: 5:42 PM
 */
interface FieldsFormatter {
    fun toFormattedText(fields: Fields): String
}