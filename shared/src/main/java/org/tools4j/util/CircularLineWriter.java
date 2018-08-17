package org.tools4j.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: ben
 * Date: 16/04/2018
 * Time: 6:02 PM
 */
public class CircularLineWriter {
    private final CircularReaderWriter cs;
    private final BufferedWriter writer;
    private final BufferedReader reader;

    public CircularLineWriter(final int bufferSize) throws IOException {
        cs = new CircularReaderWriter(bufferSize);
        writer = new BufferedWriter(cs.getWriter());
        reader = new BufferedReader(cs.getReader());
    }

    public void writeLine(final String line) throws IOException {
        writer.write(line);
        writer.newLine();
        writer.flush();
    }

    public BufferedReader getReader() {
        return reader;
    }

    public InputStream getInputStream() {
        return cs.getInput();
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }
}
