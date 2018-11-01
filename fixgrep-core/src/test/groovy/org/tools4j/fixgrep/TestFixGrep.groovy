package org.tools4j.fixgrep

import org.tools4j.fixgrep.main.FixGrep
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.utils.ArgsAsString

/**
 * User: benjw
 * Date: 30/10/2018
 * Time: 17:09
 */
class TestFixGrep {
    private static File NEW_ASSERTIONS_FILE = new File("new-assertions.txt")
    private static File RESULTS_FILE = new File("results.txt")
    private final String commonArgs
    private final boolean logNewAssertionsToFile
    private final boolean logResultsToFile
    private final boolean launchResultInBrowser

    public TestFixGrep(){
        this("", false, false, false)
    }

    public TestFixGrep(String commonArgs){
        this(commonArgs, false, false, false)
    }

    public TestFixGrep(String commonArgs, boolean launchResultInBrowser, boolean logResultsToFile, boolean logNewAssertionsToFile){
        this.launchResultInBrowser = launchResultInBrowser
        this.logResultsToFile = logResultsToFile
        this.logNewAssertionsToFile = logNewAssertionsToFile
        this.commonArgs = commonArgs

        if(logNewAssertionsToFile) deleteAndCreateNewFile(NEW_ASSERTIONS_FILE)
        if(logResultsToFile) deleteAndCreateNewFile(RESULTS_FILE)
    }

    public String go(final String fix){
        go("", fix)
    }

    public String go(String args, final String fix){
        args = args + " " + commonArgs
        final List<String> argsList = new ArgsAsString(args).toArgs()
        final CircularBufferedReaderWriter input = new CircularBufferedReaderWriter();
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();

        if(launchResultInBrowser){
            final CircularBufferedReaderWriter browserLaunchInput = new CircularBufferedReaderWriter();
            final CircularBufferedReaderWriter browserLaunchOutput = new CircularBufferedReaderWriter();

            browserLaunchInput.writer.write(fix)
            browserLaunchInput.writer.flush()
            browserLaunchInput.writer.close()

            final List<String> argsListWithLaunchBrowserFlag = new ArrayList<>(argsList)
            argsListWithLaunchBrowserFlag.add('-l')
            new FixGrep(argsListWithLaunchBrowserFlag, browserLaunchInput.inputStream, browserLaunchOutput.outputStream).go()
        }

        input.writer.write(fix)
        input.writer.flush()
        input.writer.close()

        final FixGrep fixGrep = new FixGrep(argsList, input.inputStream, output.outputStream)
        fixGrep.go()

        output.outputStream.flush()
        String lines = output.readLines('\n')

        if(logNewAssertionsToFile) {
            def testCriteriaIfActualIsCorrect = ("'" + args + "'").padRight(35) + "| '" + lines.replace("\n", "\\" + "n").replace('\u001b', '\\' + 'u001b') + "'"
            NEW_ASSERTIONS_FILE.append(testCriteriaIfActualIsCorrect + '\n')
        }

        if(logResultsToFile) {
            RESULTS_FILE.append(args)
            RESULTS_FILE.append('\n')
            RESULTS_FILE.append(lines)
            RESULTS_FILE.append('\n')
            RESULTS_FILE.append('\n')
        }
        println lines
        return lines
    }

    private void deleteAndCreateNewFile(final File file) {
        if (file) {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
        }
    }
}
