package org.tools4j.fixgrep.help

/**
 * User: ben
 * Date: 23/05/2018
 * Time: 6:16 PM
 */
interface DocWriterFactory {
    fun createNew(): DocWriter

    object ConsoleText: DocWriterFactory{
        override fun createNew(): DocWriter {
            return ConsoleTextWriter()
        }
    }

    object Html : DocWriterFactory {
        override fun createNew(): DocWriter {
            return HtmlWriter()
        }
    }
}