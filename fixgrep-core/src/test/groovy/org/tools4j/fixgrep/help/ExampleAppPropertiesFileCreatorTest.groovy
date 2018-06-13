package org.tools4j.fixgrep.help

import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 9/05/2018
 * Time: 6:21 AM
 */
class ExampleAppPropertiesFileCreatorTest extends Specification {
    @Shared final static File USER_HOME = new File(System.getProperty("user.home"))
    @Shared final static File FIXGREP_DIR = new File(USER_HOME.absolutePath + File.separator + ".fixgrep")
    @Shared final static File APP_PROPS = new File(FIXGREP_DIR.absolutePath + File.separator + "application.properties")
    private ExampleAppPropertiesFileCreator exampleAppPropertiesFileCreator;

    def setup(){
        if(FIXGREP_DIR.exists()){
            if(FIXGREP_DIR.isDirectory()) {
                println "Detected that .fixgrep dir exists, deleting..."
                FIXGREP_DIR.deleteDir()
            } else {
                println "Detected that .fixgrep exists as a file, deleting..."
                FIXGREP_DIR.delete()
            }
        }
        exampleAppPropertiesFileCreator = new ExampleAppPropertiesFileCreator();
    }

    def "CreateIfNecessary"() {
        given:
        assert !FIXGREP_DIR.exists()
        assert !APP_PROPS.exists()

        when:
        exampleAppPropertiesFileCreator.createIfNecessary()

        then:
        assert APP_PROPS.exists()
        assert APP_PROPS.text.contains("highlights")
    }

    def "Error creating .fixgrep file"() {
        given:
        //Creating the .fixgrep dir as a file!, this means that it will be impossible to create a directory at the same location
        FIXGREP_DIR.createNewFile()
        assert FIXGREP_DIR.exists()
        assert !APP_PROPS.exists()

        when:
        exampleAppPropertiesFileCreator.createIfNecessary()

        then:
        assert !APP_PROPS.exists()
    }
}
