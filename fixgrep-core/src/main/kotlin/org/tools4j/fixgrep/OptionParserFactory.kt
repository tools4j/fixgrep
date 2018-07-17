package org.tools4j.fixgrep

import joptsimple.OptionParser
import joptsimple.util.RegexMatcher.regex
import java.util.Arrays.asList
import java.util.stream.Collectors


/**
 * User: ben
 * Date: 29/03/2018
 * Time: 5:55 PM
 */
class OptionParserFactory{

    companion object {
        val optionsThatShouldNotHaveEquivalentProperties: List<String> by lazy {
            listOf("?", "help", "[arguments]", "man", "256-color-demo", "16-color-demo", "gimme-css", "html", "launch-browser", "install", "debug", "to-file", "piped-input")
        }

        val propertiesThatShouldNotBeConfigurable: List<String> by lazy {
            optionsThatShouldNotHaveEquivalentProperties.stream().map { it.replace("-", ".") }.collect(Collectors.toList())
        }
    }

    val optionParser: OptionParser by lazy {
        val parser = object: OptionParser() {
            init {
                allowsUnrecognizedOptions()
                acceptsAll(asList("a", "tag-annotations")).withRequiredArg().ofType(String::class.java).withValuesConvertedBy(regex("(none|outsideAnnotated|insideAnnotated|ba|ab|aa|bb|b_|a_|_a|_b|__)"))
                acceptsAll(asList("A", "align-columns", "align-vertical-columns")).withOptionalArg().ofType(Boolean::class.java)
                //acceptsAll(asList("g", "group-by-order")).withOptionalArg().ofType(String::class.java) // <xyz> (group messages by origClOrdId)
                acceptsAll(asList("d", "input-delimiter", "input-delim")).withRequiredArg().ofType(String::class.java) //="\u0001"
                acceptsAll(asList("e", "exclude-tags")).withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")
                acceptsAll(asList("f", "to-file")).withOptionalArg().ofType(String::class.java)
                acceptsAll(asList("F", "output-line-format")).withRequiredArg().ofType(String::class.java) // "$1 ${senderToTargetCompIdDirection} ${msgColor}[${msgTypeName}]${colorReset} ${msgFixOutsideAnnotated}"
                acceptsAll(asList("G", "line-regexgroup-for-fix")).withRequiredArg().ofType(Integer::class.java) //="2"
                acceptsAll(asList("h", "highlight", "highlights")).withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",") // 35:Blue,8:Yellow:Line,51=1:Purple:Tag,Side=Buy:Green (apply highlights based on criteria)
                acceptsAll(asList("l", "launch-browser")).withOptionalArg().ofType(Integer::class.java).withValuesSeparatedBy(",")
                acceptsAll(asList("m", "include-only-messages-of-type")).withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")
                acceptsAll(asList("n", "no-color", "suppress-colors"))
                acceptsAll(asList("o", "output-delimiter", "output-delim")).withRequiredArg().ofType(String::class.java) //="|"
                acceptsAll(asList("p", "piped", "piped-input"))
                acceptsAll(asList("q", "suppress-bold-tags-and-values"))
                acceptsAll(asList("R", "input-line-format")).withRequiredArg().ofType(String::class.java) //="^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(\\d+=.*$)"
                acceptsAll(asList("s", "sort-by-tags")).withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")
                acceptsAll(asList("t", "only-include-tags")).withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")
                acceptsAll(asList("v", "exclude-messages-of-type")).withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")
                acceptsAll(asList("x", "debug"))
                acceptsAll(asList("install"))
                acceptsAll(asList("V", "vertical-format"))
                acceptsAll(asList("256-color-demo"))
                acceptsAll(asList("16-color-demo"))
                acceptsAll(asList("man"))
                acceptsAll(asList("html")).withOptionalArg().ofType(String::class.java)
                acceptsAll(asList("gimme-css"))
                acceptsAll(asList("?", "help"))
            }
        }
        parser
    }
}