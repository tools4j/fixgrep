package org.tools4j.fixgrep

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 5:44 PM
 */
class Properties {
    val excludeRegex: String? = "AnnotatedFix"
    val fixPrefixRegex: String? = ".*?RawFix:"
    val includeRegex: String? = "35="
    val fixDelimiter: Char = '\u0001'
    val fixExtractionRegex: String? = ".*?RawFix:(.*)"
}