package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import java.util.function.Consumer

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:27 PM
 */
typealias FieldsProcessor = Consumer<Fields>
typealias StringProcessor = Consumer<String>