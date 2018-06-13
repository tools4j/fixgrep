package org.tools4j.fixgrep.html

import java.io.PrintStream

class HtmlPageFooter() {
    fun write(printStream: PrintStream) {
        val html = StringBuilder()
        html.appendln("</div>")
        html.appendln("<div class='footer'></div>")
        html.appendln("</body>").appendln("</html>")
        printStream.println(html.toString())
    }
}
