package org.tools4j.model

import org.tools4j.fix.Id
import org.tools4j.fix.SimpleId
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Supplier

/**
 * User: ben
 * Date: 7/06/2017
 * Time: 7:05 PM
 */
class IdGenerator(val prefix: String): Supplier<Id> {
    private val execId = AtomicLong(1)

    override fun get(): Id {
        return SimpleId(prefix + execId.getAndIncrement());
    }
}
