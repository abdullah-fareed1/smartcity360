package lk.urbantech.smartcity360.security.pattern;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Decorator pattern role: CONCRETE DECORATOR.
 * XOR "encryption" is a DEMO only. A real system would use AES.
 */
public class EncryptionDecorator extends StreamDecorator {

    private static final byte[] KEY =
            "UrbanTech-Demo-Key-2026-SmartCity360".getBytes(StandardCharsets.UTF_8);

    public EncryptionDecorator(DataStream wrapped) {
        super(wrapped);
    }

    @Override
    public String process(String data) {
        byte[] input = wrapped.process(data).getBytes(StandardCharsets.UTF_8);   // delegate FIRST
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] ^ KEY[i % KEY.length]);
        }
        return Base64.getEncoder().encodeToString(output);
    }
}