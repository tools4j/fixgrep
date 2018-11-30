package org.tools4j.fix

import org.tools4j.fix.spec.FixSpecDefinition
import java.util.*

/**
 * User: benjw
 * Date: 7/16/2018
 * Time: 5:06 PM
 */
interface FieldVisitor {
    fun visit(field: Field)
}