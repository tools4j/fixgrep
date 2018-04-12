package org.tools4j.fixgrep

import joptsimple.OptionParser
import joptsimple.OptionSet
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 3/04/2018
 * Time: 9:05 AM
 *
 *
 acceptsAll(asList("n", "no-color", "no-colour"))
 acceptsAll(asList("h", "highlight")).withRequiredArg().ofType(String.javaClass).withValuesSeparatedBy(",") // 35:Blue,8:Yellow:Line,51=1:Purple:Tag,Side=Buy:Green (apply highlights based on criteria)
 acceptsAll(asList("g", "group-by-order")).withOptionalArg().ofType(String.javaClass) // <xyz> (group messages by origClOrdId)
 acceptsAll(asList("d", "input-delimiter", "input-delim")).withRequiredArg().ofType(Char.javaClass) //="\u0001"
 acceptsAll(asList("o", "output-delimiter", "output-delim")).withRequiredArg().ofType(Char.javaClass) //="|"
 acceptsAll(asList("l", "line-format")).withRequiredArg().ofType(String.javaClass) // "$1 ${senderToTargetCompIdDirection} ${msgColor}[${msgTypeName}]${colorReset} ${msgFixOutsideAnnotated}"
 acceptsAll(asList("r", "line-regex")).withRequiredArg().ofType(String.javaClass) //="^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?(\\d+=.*$)"
 acceptsAll(asList("i", "line-regexgroup-for-fix")).withRequiredArg().ofType(Int.javaClass) //="2"
 acceptsAll(asList("s", "sort-by-tags")).withRequiredArg().ofType(Int.javaClass).withValuesSeparatedBy(",")
 acceptsAll(asList("i", "only-include-tags")).withRequiredArg().ofType(Int.javaClass).withValuesSeparatedBy(",")
 acceptsAll(asList("e", "exclude-tags")).withRequiredArg().ofType(Int.javaClass).withValuesSeparatedBy(",")
 acceptsAll(asList("f", "tag-format")).withRequiredArg().ofType(String.javaClass).withValuesConvertedBy(regex("(raw|outsideAnnotated|insideAnnotated|ba|ab|aa|bb|b_|a_|_a|_b|__)"))
 acceptsAll(asList("v", "vertical-format"))
 acceptsAll(asList("?", "help"));
 *
 */
class OptionParserFactoryTest extends Specification {
    def "SetupOptionParser grouped options"() {
        given:
        final OptionParser optionParser = new OptionParserFactory().optionParser

        when:
        final OptionSet options = optionParser.parse("-nv")

        then:
        assert options.has("n")
        assert options.has("no-color")
        assert options.has("v")
        assert options.has("vertical-format")
    }

    def "SetupOptionParser non-option argument"() {
        given:
        final OptionParser optionParser = new OptionParserFactory().optionParser

        when:
        final OptionSet options = optionParser.parse("-n", "asdf")

        then:
        assert options.has("n")
        assert options.nonOptionArguments().size() == 1
        assert options.nonOptionArguments().get(0) == 'asdf'
    }

    @Unroll
    def "SetupOptionParser tag-annotations.  e.g. outsideAnnotated etc"(final String optionArgument) {
        given:
        final OptionParser optionParser = new OptionParserFactory().optionParser

        when:
        final OptionSet options = optionParser.parse("-a", optionArgument)

        then:
        assert options.has('a')
        assert options.has('tag-annotations')
        assert options.hasArgument('a')
        assert options.valueOf('a') == optionArgument

        where:
        optionArgument | _
        'outsideAnnotated'| _
        'insideAnnotated'| _
        'ba'| _
        'ab'| _
        'aa'| _
        'bb'| _
        'b_'| _
        'a_'| _
        '_a'| _
        '_b'| _
        '__'| _
        'none'| _
    }

    def "SetupOptionParser tag-annotations.  e.g. illegal option"() {
        given:
        final OptionParser optionParser = new OptionParserFactory().optionParser

        when:
        final OptionSet options = optionParser.parse("-a", "blah")
        options.valueOf("a")

        then:
        final Exception e = thrown()
        assert e.getMessage().contains("Cannot parse argument 'blah' of option")
    }

    def "SetupOptionParser highlights"() {
        given:
        final OptionParser optionParser = new OptionParserFactory().optionParser

        when:
        final OptionSet options = optionParser.parse("--highlights", "35:Blue,8:Yellow:Line,51=1:Purple:Tag,Side=Buy:Green")

        then:
        assert options.has('h')
        final List<?> arguments = options.valuesOf('h')
        assert arguments.get(0) == '35:Blue'
        assert arguments.get(1) == '8:Yellow:Line'
        assert arguments.get(2) == '51=1:Purple:Tag'
        assert arguments.get(3) == 'Side=Buy:Green'
    }
}
