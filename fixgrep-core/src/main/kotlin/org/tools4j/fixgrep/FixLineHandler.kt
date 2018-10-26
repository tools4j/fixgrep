package org.tools4j.fixgrep

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
interface FixLineHandler{
    fun handle(fixLine: FixLine)
    fun finish()
}