package org.tools4j.fixgrep

import mu.KLogging
import org.junit.jupiter.api.Test
import java.io.File

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class MyTest {
    companion object: KLogging()

    @Test
    fun myTest(){
        val workingDir = System.getProperty("user.dir")
        val pathPrefix: String = getPathPrefix()
        BashRunner().run(5000, "cd '$workingDir' && cd ${pathPrefix}/build/dist/files && ./fixgrep -x test2.log")
    }

    private fun getPathPrefix(): String {
        val pathPrefix: String
        if (File("./fixgrep-core").exists()) {
            logger.info { "Detected that we are in the root of the project..." }
            pathPrefix = "fixgrep-core"
        } else if (File("../fixgrep-core").exists()) {
            logger.info { "Detected that we are in the fixgrep-core dir of the project..." }
            pathPrefix = "."
        } else {
            throw IllegalStateException("Cannot work out where we are in the project.  Current dir: [" + File(".").absolutePath + "]")
        }
        return pathPrefix
    }
}