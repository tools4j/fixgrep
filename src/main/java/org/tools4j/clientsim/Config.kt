package org.tools4j.clientsim

/**
 * User: ben
 * Date: 8/06/2017
 * Time: 5:19 PM
 */
class Config(
        val minSliceQty: Long,
        val averageAmendPriceAsFractionOfTotalPrice: Double,
        val averageFillQtyAsFractionOfTotalOrderQty: Double,
        val averageTotalOrderSize: Long,
        val possibleInstruments: List<String>,
        val possibilityOfUnsolCancelPerEvaluation: Double,
        val possibilityOfAmendPerEvaluation: Double,
        val possibilityOfAmendFailing: Double,
        val averageAmendQtyAsFractionOfTotalOrderQty: Double,
        val possibilityOfPricedOrder: Double,
        val newOrderPriceDeviation: Double,
        val newOrderAveragePrice: Double,
        val possibilityOfNosFailing: Double,
        val possibilityOfCancelPerEvaluation: Double,
        val possibilityOfCancelFailing: Double,
        val possibilityOrNewOrderCreatedPerEvaluation: Double) {

    fun getMessageSpacing(times: Int): Long {
        return 0
    }
}
