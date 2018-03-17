package org.tools4j.fixgrep

import org.tools4j.fix.ClasspathResource
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class FixGrepTest extends Specification {
    def "run fixgrep"(){
        expect:
        new FixGrep(this.class.getResourceAsStream("/test.log"), new Properties()).go()
    }
}
