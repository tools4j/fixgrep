package org.tools4j.fixgrep

import org.tools4j.fix.Fields
import java.util.function.Consumer

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:09 AM
 */
class LabellingStringProcessor(val originalFields: Fields, val labels: List<Label>, output: Consumer<String>): AbstractStringProcessingPipe(output) {
    override fun accept(inputStr: String) {
        var outputStr = inputStr
        labels.forEach {
            if(it.shouldLabel(originalFields, outputStr)){
                outputStr = it.labelAndReturnNewLine(originalFields, outputStr)
            }
        }
        output.accept(outputStr)
    }
}