package org.tools4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * User: ben
 * Date: 16/04/2018
 * Time: 6:02 PM
 */
public class CircularStream {
    private final PipedInputStream input;
    private final PipedOutputStream output;

    public CircularStream(final int size) throws IOException {
        input = new PipedInputStream(size);
        output = new PipedOutputStream(input);
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return output;
    }
}
