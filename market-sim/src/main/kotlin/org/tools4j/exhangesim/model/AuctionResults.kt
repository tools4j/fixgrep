package org.tools4j.exhangesim.model;

/**
 * User: ben
 * Date: 26/10/2016
 * Time: 5:29 PM
 */
class AuctionResults(val lowestCrossingPrice: Double?, val highestCrossingPrice: Double?, val highestVolumeCrossingPrice: Double?, val highestCrossingVolume: Long?) {
    override fun toString(): String {
        return "AuctionResults{" +
                "lowestCrossingPrice=" + lowestCrossingPrice +
                ", highestCrossingPrice=" + highestCrossingPrice +
                ", highestVolumeCrossingPrice=" + highestVolumeCrossingPrice +
                ", highestCrossingVolume=" + highestCrossingVolume +
                '}'.toString()
    }
}
