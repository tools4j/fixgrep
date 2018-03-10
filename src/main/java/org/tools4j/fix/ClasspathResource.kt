package org.tools4j.fix

import java.io.InputStream

/**
 * User: ben
 * Date: 29/6/17
 * Time: 5:45 PM
 */
class ClasspathResource(private val path: String) : FileResource {

    override fun asInputStream(): InputStream {
        return this.javaClass.getResourceAsStream(path)
    }
}
