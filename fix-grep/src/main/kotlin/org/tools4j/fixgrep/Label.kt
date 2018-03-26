package org.tools4j.fixgrep

import java.util.function.Supplier

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:12 AM
 */
interface Label {
    fun shouldLabel(messageString: MessageString): Boolean
    fun labelAndReturnNewLine(messageString: MessageString): MessageString

    class SimpleNosLabel(searchRegex: String, replaceWith: String): SimpleLabel(LabellingCriteria.Nos(), searchRegex, replaceWith)
    class SimpleNewLabel(searchRegex: String, replaceWith: String): SimpleLabel(LabellingCriteria.New(), searchRegex, replaceWith)
    class SimpleCanceledLabel(searchRegex: String, replaceWith: String): SimpleLabel(LabellingCriteria.Canceled(), searchRegex, replaceWith)
    class SimplePendingNewLabel(searchRegex: String, replaceWith: String): SimpleLabel(LabellingCriteria.PendingNew(), searchRegex, replaceWith)

    class SimpleLabelsWithReplaceAfterOptionalDate: Supplier<List<Label>> {
        override fun get(): List<Label> {
            val dateRegex = "^((\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d+) ?)?"
            return arrayListOf(
                    SimpleNosLabel(dateRegex, "$2 [NewOrderSingle] "),
                    SimpleNewLabel(dateRegex, "$2 [New] "),
                    SimpleCanceledLabel(dateRegex, "$2 [Canceled] "),
                    SimplePendingNewLabel(dateRegex, "$2 [PendingNew] ")
            )
        }
    }
}