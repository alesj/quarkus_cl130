package com.alesj.qcl.app;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Provider
@Priority(1)
@ApplicationScoped
public class ResponseInterceptor implements ContainerResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(ResponseInterceptor.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        log.info("{} {}", responseContext.getStatusInfo().getStatusCode(), responseContext.getStatusInfo().toEnum());
        var headers = responseContext.getHeaders();
        if (headers != null) {
            headers
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream().map(v -> Map.entry(e.getKey(), v)))
                .forEach(e -> log.info("{}: {}", e.getKey(), e.getValue()));
        }
        log.info("");
        var entityStream = responseContext.getEntityStream();
        if (entityStream != null) {
            responseContext.setEntityStream(
                new DebugOutputStream(
                    entityStream,
                    log::info,
                    StandardCharsets.UTF_8
                )
            );
        }
    }

    private static class DebugOutputStream extends FilterOutputStream {
        private final Consumer<String> debugOutput;
        private final Charset charset;
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        public DebugOutputStream(OutputStream out, Consumer<String> debugOutput, Charset charset) {
            super(out);
            this.debugOutput = debugOutput;
            this.charset = charset;
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            if (b == '\n') {
                var s = baos.toString(charset);
                baos.reset();
                debugOutput.accept(s);
            } else {
                baos.write(b);
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            Objects.checkFromIndexSize(off, len, b.length);
            out.write(b, off, len);
            int start = off;
            int end = off;
            while (end < off + len) {
                if (b[end] == '\n') {
                    baos.write(b, start, end - start);
                    var s = baos.toString(charset);
                    baos.reset();
                    debugOutput.accept(s);
                    start = end + 1;
                    end = start;
                } else {
                    end++;
                }
            }
            if (end > start) {
                baos.write(b, start, end - start);
            }
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (baos.size() > 0) {
                var s = baos.toString(charset);
                baos.reset();
                debugOutput.accept(s);
            }
        }

    }
}