package org.tools4j.fixgrep.html

import org.tools4j.fix.ClasspathResource
import java.io.PrintStream

class HtmlPageHeader(val heading: String, val displayToc: Boolean = false) {
    fun write(printStream: PrintStream) {
        val html = StringBuilder()
        html.appendln("""<html>
                            <head>
                            <title>fixgrep</title>
                            <style>""")

        html.append(ClasspathResource("/fixgrep.css").toString()).append("\n")
                .appendln("</style>")

        if(displayToc){
            html.appendln("<script type='text/javascript'>")
            html.append(ClasspathResource("/fixgrep.js").toString()).append("\n")
            html.appendln("</script>")
        }

        html.appendln("</head>")
        html.appendln("<body>")
        html.appendln("<div class='top-strip'>&nbsp;</div>")
                .appendln("<div class='header'>")
                .appendln("<span>$heading</span>")
                .appendln("</div>")

        if(displayToc){
            html.appendln("<div id='toc'></div>")
        }

        html.appendln("<div class='content' id='contents'>")
        printStream.println(html.toString())
    }
}
