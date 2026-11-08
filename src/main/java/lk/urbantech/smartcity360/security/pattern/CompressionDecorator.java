package lk.urbantech.smartcity360.security.pattern;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

/** Decorator pattern role: CONCRETE DECORATOR. GZIP-compresses the data (as Base64 text). */
public class CompressionDecorator extends StreamDecorator {

    public CompressionDecorator(DataStream wrapped) {
        super(wrapped);
    }

    @Override
    public String process(String data) {
        String input = wrapped.process(data);          // delegate FIRST, then add our behaviour
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(bytes)) {
            gzip.write(input.getBytes(StandardCharsets.UTF_8));
            gzip.finish();
            return Base64.getEncoder().encodeToString(bytes.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Compression failed", e);
        }
    }
}