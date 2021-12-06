package com.alesj.qcl.common.log;

import org.jboss.logmanager.EmbeddedConfigurator;
import org.jboss.logmanager.formatters.PatternFormatter;
import org.jboss.logmanager.handlers.WriterHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Handler;
import java.util.logging.Level;

public class SimpleEmbeddedConfigurator implements EmbeddedConfigurator {

    public SimpleEmbeddedConfigurator() {
        System.out.println("SimpleEmbeddedConfigurator!");
    }

    @Override
    public Level getMinimumLevelOf(final String loggerName) {
        return Level.ALL;
    }

    @Override
    public Level getLevelOf(final String loggerName) {
        return loggerName.isEmpty() ? Level.ALL : null;
    }

    @Override
    public Handler[] getHandlersOf(final String loggerName) {
        if (loggerName.isEmpty()) {
            return new Handler[]{createDefaultHandler()};
        } else {
            return EmbeddedConfigurator.NO_HANDLERS;
        }
    }

    public static Handler createDefaultHandler() {
        // OutputStream that delegates to "current at the time of invocation" System.out
        var stream = new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                System.out.write(i);
            }

            @Override
            public void write(byte[] b) throws IOException {
                System.out.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                System.out.write(b, off, len);
            }

            @Override
            public void flush() throws IOException {
                System.out.flush();
            }
        };

        var handler = new WriterHandler();
        handler.setWriter(new OutputStreamWriter(stream));
        handler.setFormatter(new PatternFormatter("%d{HH:mm:ss,SSS} %-5p [%c{3.}] %s%e%n"));
        handler.setLevel(Level.INFO);
        return handler;
    }
}
