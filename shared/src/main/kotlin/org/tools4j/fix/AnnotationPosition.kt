package org.tools4j.fix

/**
 * User: ben
 * Date: 14/05/2018
 * Time: 5:33 PM
 */
enum class AnnotationPosition(val abbrev: Char) {
    REPLACE('r'),
    BEFORE('b'),
    AFTER('a'),
    NONE('_');
}