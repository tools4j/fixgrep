package org.tools4j.fixgrep

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:09 AM
 */
class LabellingMessageStringProcessor(val labels: List<Label>, output: MessageStringProcessor): AbstractMessageStringProcessingPipe(output) {
    override fun accept(messageString: MessageString) {
        var outputString = messageString
        labels.forEach {
            if(it.shouldLabel(outputString)){
                outputString = it.labelAndReturnNewLine(outputString)
            }
        }
        output.accept(outputString)
    }
}