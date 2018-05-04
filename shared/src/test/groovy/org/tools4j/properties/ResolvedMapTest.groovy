package org.tools4j.properties

import spock.lang.Specification

/**
 * User: ben
 * Date: 26/10/17
 * Time: 5:58 PM
 */
class ResolvedMapTest extends Specification {
    private Map<String, String> map;

    def setup(){
        map = new HashMap<>();
        map.put('connection.host', 'haud0001');
        map.put('connection.username', 'me');
        map.put('connection.port', '8080');
        map.put('connection.password', 'secret');
        map.put('password', '${connection.password}');
        map.put('nested1', 'host')
        map.put('nested2', 'and.port')
        map.put('host.and.port', '${connection.host}:${connection.port}');
        map.put('username.and.password', '${connection.username}:${connection.password}');
        map.put('fullUrl', '${host.and.port}:${username.and.password}');
        map.put('fullUrl.with.nested.variables', '${${nested1}.${nested2}}:${username.and.password}');
        map.put('missing.variables1', '${${missing1}.${missing2}}:${connection.host}:${missing3}:${connection.port}');
        map.put('missing.variables2', '${${nested2}.${nested1}}:${connection.host}:${missing3}:${connection.port}');
        map.put('escaped.dollar', 'blah \\${connection.host} blah');
    }


    def "test Resolve"() {
        when:
        final Map<String, String> resolved = new ResolvedMap(map).resolve();

        then:
        final Map<String, String> expected = [
                'connection.host'              : 'haud0001',
                'connection.username'          : 'me',
                'connection.port'              : '8080',
                'connection.password'          : 'secret',
                'password'                     : 'secret',
                'nested1'                      : 'host',
                'nested2'                      : 'and.port',
                'host.and.port'                : 'haud0001:8080',
                'username.and.password'        : 'me:secret',
                'fullUrl'                      : 'haud0001:8080:me:secret',
                'fullUrl.with.nested.variables': 'haud0001:8080:me:secret',
                'missing.variables1'           : '${${missing1}.${missing2}}:haud0001:${missing3}:8080',
                'missing.variables2'           : '${and.port.host}:haud0001:${missing3}:8080',
                'escaped.dollar'               : 'blah \\${connection.host} blah'
        ]

        assert resolved == expected
    }

    def "test Resolve using secondaryMap"() {
        given:
        final Map<String, String> secondaryMap = [
                'missing1': 'howdy',
                'missing2': 'partner',
                'howdy.partner': '${username.and.password}'
        ]

        when:
        final Map<String, String> resolved = new ResolvedMap(map, secondaryMap).resolve();

        then:
        final Map<String, String> expected = [
                'connection.host'              : 'haud0001',
                'connection.username'          : 'me',
                'connection.port'              : '8080',
                'connection.password'          : 'secret',
                'password'                     : 'secret',
                'nested1'                      : 'host',
                'nested2'                      : 'and.port',
                'host.and.port'                : 'haud0001:8080',
                'username.and.password'        : 'me:secret',
                'fullUrl'                      : 'haud0001:8080:me:secret',
                'fullUrl.with.nested.variables': 'haud0001:8080:me:secret',
                'missing.variables1'           : 'me:secret:haud0001:${missing3}:8080',
                'missing.variables2'           : '${and.port.host}:haud0001:${missing3}:8080',
                'escaped.dollar'               : 'blah \\${connection.host} blah'
        ]

        assert resolved == expected
    }
}
