package org.tools4j.fixgrep.utils

import java.io.Closeable

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 04:02
 */
interface LineReader: Closeable {
    fun readLine(): String?
}