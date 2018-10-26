package org.tools4j.fixgrep.orders;

/**
 * User: benjw
 * Date: 05/10/2018
 * Time: 06:17
 */
class UniqueIdSpecs(
    val uniqueClientOrderIdSpec: UniqueClientOrderIdSpec,
    val uniqueOriginalClientOrderIdSpec: UniqueOriginalClientOrderIdSpec,
    val uniqueOrderIdSpec: UniqueOrderIdSpec){

    constructor(): this(UniqueClientOrderIdSpec(), UniqueOriginalClientOrderIdSpec(), UniqueOrderIdSpec())
}
