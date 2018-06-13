package org.tools4j.fixgrep

import org.tools4j.properties.Config
import org.tools4j.properties.ConfigAndArguments
import org.tools4j.properties.ConfigImpl
import org.tools4j.properties.ConfigLoader

/**
 * User: ben
 * Date: 4/05/2018
 * Time: 8:48 AM
 */
class  TestConfigBuilder {
    public static Config load(){
        return new ConfigImpl(ConfigLoader.fromClasspath("application.properties"))
    }
}
