package org.tools4j.fixgrep.perf

/**
 * User: ben
 * Date: 22/06/2018
 * Time: 6:13 AM
 */
class CreateVeryLargeFileForPerfTesting {
    public static void main(String[] args) {
        println new File("src/test/resources/test.log").absolutePath

        createTestFile()
    }

    static void createTestFile() {
        final File inputFile = new File("fixgrep-core/src/test/resources/test.log")
        final File outputFile = new File("/var/tmp/fixgrep-largefile.log")
        if(outputFile.exists()) {
            outputFile.delete()
        }

        outputFile.createNewFile()
        final Writer writer = outputFile.newWriter()
        def inputFileText = inputFile.text
        for(int i=0; i<10000; i++){
            writer.write(inputFileText)
            if(i % 100 == 0) println "Written: $i"
        }
        writer.flush()
        writer.close()
    }
}
