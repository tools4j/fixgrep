package org.tools4j.fix

/**
 * User: ben
 * Date: 7/06/2017
 * Time: 6:46 AM
 */
enum class OrdStatus private constructor(val code: String) {
    New("0"),
    PartiallyFilled("1"),
    Filled("2"),
    DoneForDay("3"),
    Canceled("4"),
    ReplacedDeprecated("5"),
    PendingCancel("6"),
    Stopped("7"),
    Rejected("8"),
    Suspended("9"),
    PendingNew("A"),
    Calculated("B"),
    Expired("C"),
    AcceptedForBidding("D"),
    PendingReplace("E");

    companion object{
        val codeToOrderStatus: Map<String, OrdStatus> by lazy {
            val map = HashMap<String, OrdStatus>()
            OrdStatus.values().forEach { map.put(it.code, it) }
            map
        }

        fun forCode(code: String): OrdStatus {
            return codeToOrderStatus[code]!!
        }
    }
}
