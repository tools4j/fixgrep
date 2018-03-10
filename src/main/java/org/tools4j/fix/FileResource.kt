package org.tools4j.fix

import java.io.InputStream

/**
 * User: ben
 * Date: 29/6/17
 * Time: 5:46 PM
 */
interface FileResource {
    fun asInputStream(): InputStream
}
