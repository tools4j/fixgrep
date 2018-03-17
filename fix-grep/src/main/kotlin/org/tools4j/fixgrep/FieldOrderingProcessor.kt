package org.tools4j.fixgrep

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:01 PM
 */
class FieldOrderingProcessor(val desiredOrder: List<Int>, output: FieldsProcessor): AbstractFieldsProcessingPipe(output) {
    override fun accept(inputFieldsSource: Fields) {
        output.accept(inputFieldsSource.sortBy(desiredOrder))
    }
}