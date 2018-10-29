package org.tools4j.fixgrep.linehandlers

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:15 PM
 */
interface LineHandler {
    fun handle(line: String)
    fun finish()
}