package org.tools4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * User: ben
 * Date: 16/04/2018
 * Time: 6:02 PM
 */
public class CircularReaderWriter {
    private final CircularStream cs;
    private final Writer writer;
    private final Reader reader;

    public CircularReaderWriter() throws IOException {
        cs = new CircularStream();
        writer = new OutputStreamWriter(cs.getOutput());
        reader = new InputStreamReader(cs.getInput());
    }

    public Writer getWriter() {
        return writer;
    }

    public Reader getReader() {
        return reader;
    }

    public InputStream getInput() {
        return cs.getInput();
    }

    public OutputStream getOutput() {
        return cs.getOutput();
    }
}
