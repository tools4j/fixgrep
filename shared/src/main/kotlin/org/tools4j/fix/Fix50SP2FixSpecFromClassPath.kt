package org.tools4j.fix

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:04 PM
 */
class Fix50SP2FixSpecFromClassPath : FixSpecFromClassPath("/FIX50SP2-fields-and-enums.properties", "/FIX50SP2-header.properties", "/FIX50SP2-trailer.properties", "/FIX50SP2-messages.properties"){
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val before = System.currentTimeMillis()
            println(Fix50SP2FixSpecFromClassPath().spec)
            val after = System.currentTimeMillis()
            println(after - before)
        }
    }
}
