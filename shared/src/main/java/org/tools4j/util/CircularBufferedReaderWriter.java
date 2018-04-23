package org.tools4j.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * User: ben
 * Date: 16/04/2018
 * Time: 6:02 PM
 */
public class CircularBufferedReaderWriter {
    private final CircularReaderWriter cs;
    private final BufferedWriter writer;
    private final BufferedReader reader;

    public CircularBufferedReaderWriter() throws IOException {
        cs = new CircularReaderWriter();
        writer = new BufferedWriter(cs.getWriter());
        reader = new BufferedReader(cs.getReader());
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public InputStream getInputStream() {
        return cs.getInput();
    }

    public OutputStream getOutputStream() {
        return cs.getOutput();
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public String readLines(String joinDelimiter) throws IOException {
        final StringJoiner joiner = new StringJoiner(joinDelimiter);
        readLines().forEach(joiner::add);
        return joiner.toString();
    }

    public List<String> readLines() throws IOException {
        final List<String> lines = new ArrayList<>();
        while(true){
            final String line = readLine();
            if(line == null) break;
            lines.add(line);
        }
        return lines;
    }
}
