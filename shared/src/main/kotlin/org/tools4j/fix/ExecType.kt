package org.tools4j.fix

/**
 * User: ben
 * Date: 7/06/2017
 * Time: 6:38 AM
 */
enum class ExecType(val code: String) {
    New("0"),
    PartialFill("1"),
    Fill("2"),
    DoneForDay("3"),
    Canceled("4"),
    Replaced("5"),
    PendingCancel("6"),
    Stopped("7"),
    Rejected("8"),
    Suspended("9"),
    PendingNew("A"),
    Calculated("B"),
    Expired("C"),
    PendingReplace("E"),
    Trade("F"),
    TradeCorrect("G"),
    TradeCancel("H"),
    OrderStatus("I"),
    TradeInAClearingHold("J"),
    TradeHasBeenReleasedToClearing("K"),
    TriggeredOrActivatedBySystem("L");

    companion object{
        val codeToExecTypeMap: Map<String, ExecType> by lazy {
            val map = HashMap<String, ExecType>()
            ExecType.values().forEach { map.put(it.code, it) }
            map
        }

        fun forCode(code: String): ExecType {
            return codeToExecTypeMap[code]!!
        }
    }
}
