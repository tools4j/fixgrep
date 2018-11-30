package org.tools4j.fixgrep.help

import spock.lang.Specification

/**
 * User: benjw
 * Date: 26/11/2018
 * Time: 09:58
 */
class SingleExampleTest extends Specification {
    def "ToFormattedString - Html"() {
        when:
        final SingleExample singleExample = new SingleExample(
                "8=FIX.5.2^A9=232^A35=D^A11=C28^A55=AUD/USD^A54=2^A38=1464820^A44=100.026\n8=FIX.5.2^A9=54^A35=8^A11=C28^A150=0^A151=1464820^A14=0^A44=100.02",
                "-e 8,9 -V -d ^A",
                new DocWriterFactory.Html());

        then:
        assert singleExample.toFormattedString() == """<div class='example-list'>
<div class='console'>
<div class='msg-header'>
================================================================================</br>
<span class='FgCyan'>NewOrderSingle</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>[MsgType]</span><span class='tag raw bold'>35</span><span class='equals bold'>=</span><span class='value raw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>[ClOrdID]</span><span class='tag raw bold'>11</span><span class='equals bold'>=</span><span class='value raw bold'>C28</span></div><!--uid:3-->
<div class='field annotatedField'><!--uid:4--><span class='tag annotation'>[Symbol]</span><span class='tag raw bold'>55</span><span class='equals bold'>=</span><span class='value raw bold'>AUD/USD</span></div><!--uid:5-->
<div class='field annotatedField'><!--uid:6--><span class='tag annotation'>[Side]</span><span class='tag raw bold'>54</span><span class='equals bold'>=</span><span class='value raw bold'>2</span><span class='value annotation'>[SELL]</span></div><!--uid:7-->
<div class='field annotatedField'><!--uid:8--><span class='tag annotation'>[OrderQty]</span><span class='tag raw bold'>38</span><span class='equals bold'>=</span><span class='value raw bold'>1464820</span></div><!--uid:9-->
<div class='field annotatedField'><!--uid:10--><span class='tag annotation'>[Price]</span><span class='tag raw bold'>44</span><span class='equals bold'>=</span><span class='value raw bold'>100.026</span></div><!--uid:11-->
</div>

<br/>
<div class='msg-header'>
================================================================================</br>
<span class='FgGreen'>Exec.New</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>[MsgType]</span><span class='tag raw bold'>35</span><span class='equals bold'>=</span><span class='value raw bold'>8</span><span class='value annotation'>[EXECUTIONREPORT]</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>[ClOrdID]</span><span class='tag raw bold'>11</span><span class='equals bold'>=</span><span class='value raw bold'>C28</span></div><!--uid:3-->
<div class='field annotatedField'><!--uid:4--><span class='tag annotation'>[ExecType]</span><span class='tag raw bold'>150</span><span class='equals bold'>=</span><span class='value raw bold'>0</span><span class='value annotation'>[NEW]</span></div><!--uid:5-->
<div class='field annotatedField'><!--uid:6--><span class='tag annotation'>[LeavesQty]</span><span class='tag raw bold'>151</span><span class='equals bold'>=</span><span class='value raw bold'>1464820</span></div><!--uid:7-->
<div class='field annotatedField'><!--uid:8--><span class='tag annotation'>[CumQty]</span><span class='tag raw bold'>14</span><span class='equals bold'>=</span><span class='value raw bold'>0</span></div><!--uid:9-->
<div class='field annotatedField'><!--uid:10--><span class='tag annotation'>[Price]</span><span class='tag raw bold'>44</span><span class='equals bold'>=</span><span class='value raw bold'>100.02</span></div><!--uid:11-->
</div>

<br/></div>
</div>
"""
    }

    def "ToFormattedString - Console"() {
        when:
        final SingleExample singleExample = new SingleExample(
                "8=FIX.5.2^A9=232^A35=D^A11=C28^A55=AUD/USD^A54=2^A38=1464820^A44=100.026\n8=FIX.5.2^A9=54^A35=8^A11=C28^A150=0^A151=1464820^A14=0^A44=100.02",
                "-e 8,9 -V -d ^A",
                new DocWriterFactory.ConsoleText());

        then:
        assert singleExample.toFormattedString() == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]
[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mC28\u001B[22m
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
[Side]\u001B[1m54\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m[SELL]
[OrderQty]\u001B[1m38\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1464820\u001B[22m
[Price]\u001B[1m44\u001B[22m\u001B[1m=\u001B[22m\u001B[1m100.026\u001B[22m

================================================================================
\u001B[32mExec.New\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]
[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mC28\u001B[22m
[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
[LeavesQty]\u001B[1m151\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1464820\u001B[22m
[CumQty]\u001B[1m14\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m
[Price]\u001B[1m44\u001B[22m\u001B[1m=\u001B[22m\u001B[1m100.02\u001B[22m


"""
    }
}
