package org.tools4j.properties

import spock.lang.Specification

/**
 * User: ben
 * Date: 25/10/17
 * Time: 5:35 PM
 */
class ConfigTest extends Specification {
    private Config repo;

    def setup(){
        final Properties properties = new Properties();
        properties.put('red.truck.weight', 'rtw');
        properties.put('red.truck.cab.weight', 'rtcw');
        properties.put('red.truck.trailer.weight', 'rttw');
        properties.put('red.truck.trailer.registration', 'rtrr');
        properties.put('red.car.weight', 'rcw');
        properties.put('red.car.registration', 'rcr');
        properties.put('red.car.engine.weight', 'rcew');
        properties.put('red.car.engine.registration', 'rcer');
        properties.put('black.truck.weight', 'btw');
        properties.put('black.truck.cab.weight', 'btcw');
        properties.put('black.truck.trailer.weight', 'bttw');
        properties.put('black.truck.trailer.registration', 'btrr');
        properties.put('black.car.weight', 'bcw');
        properties.put('black.car.registration', 'bcr');
        properties.put('black.car.engine.weight', 'bcew');
        properties.put('black.car.engine.registration', 'bcer');
        properties.put('escaped.variable', '\\${hi.there}');
        properties.put('escaped.escaped.variable', '\\\\${hi.there}');
        repo = new Config(properties);
    }

    def "Get escaped variable"(){
        expect:
        assert repo.get('escaped.variable') == '${hi.there}'
        assert repo.get('escaped.escaped.variable') == '\\${hi.there}'
    }

    def "test clean variable of escaped variables"(){
        expect:
        repo.cleanValueOfEscapedVariables('\\${hi.there}') == '${hi.there}'
        repo.cleanValueOfEscapedVariables('\\${\\${hi}.${there}}') == '${${hi}.${there}}'
    }


    def "GetWithPrefix 1"() {
        when:
        final Config redRepo = repo.getWithPrefix("red");

        then:
        assert redRepo.size() == 8;
        assert redRepo.get("truck.weight") == "rtw"
        assert redRepo.get("truck.cab.weight") == "rtcw"
        assert redRepo.get("truck.trailer.weight") == "rttw"
        assert redRepo.get("truck.trailer.registration") == "rtrr"
        assert redRepo.get("car.weight") == "rcw"
        assert redRepo.get("car.registration") == "rcr"
        assert redRepo.get("car.engine.weight") == "rcew"
        assert redRepo.get("car.engine.registration") == "rcer"
    }

    def "GetWithPrefix 3"() {
        given:
        final Config blackCarRepo = repo.getWithPrefix("black.car");
        assert blackCarRepo.get("weight") == "bcw"
        assert blackCarRepo.get("registration") == "bcr"
        assert blackCarRepo.get("engine.weight") == "bcew"
        assert blackCarRepo.get("engine.registration") == "bcer"

        final Config blackCarEngineRepo = blackCarRepo.getWithPrefix("engine");
        assert blackCarEngineRepo.size() == 2
        assert blackCarEngineRepo.get("weight") == "bcew"
        assert blackCarEngineRepo.get("registration") == "bcer"
    }

    def "test getNextUniqueKeyParts"() {
        given:
        assert repo.getNextUniqueKeyParts() == new HashSet(["red", "black", "escaped"]);
        assert repo.getWithPrefix("red.truck").getNextUniqueKeyParts() == new HashSet(["weight", "cab", "trailer"]);
    }
}
