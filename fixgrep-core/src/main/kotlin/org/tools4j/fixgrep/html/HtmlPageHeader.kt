package org.tools4j.fixgrep.html

import org.tools4j.fix.ClasspathResource
import java.io.PrintStream

class HtmlPageHeader(val heading: String) {
    fun write(printStream: PrintStream) {
        val html = StringBuilder()
        html.append("""<html>
                            <head>
                            <title>fixgrep</title>
                            <style>\n""")

        html.append(ClasspathResource("/fixgrep.css").toString()).append("\n")
                .appendln("</style>")
                .appendln("</head>")
                .appendln("<body class='fix-output'>")
                .appendln("<div class='header'>")
                .appendln("<h1>")
                .appendln(heading)
                .appendln("</h1>")
                .appendln("<div class='console'>")
        printStream.println(html.toString())
    }
}
