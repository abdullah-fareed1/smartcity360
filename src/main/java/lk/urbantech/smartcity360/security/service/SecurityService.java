package lk.urbantech.smartcity360.security.service;

import lk.urbantech.smartcity360.security.pattern.AuthDecorator;
import lk.urbantech.smartcity360.security.pattern.BasicMeterStream;
import lk.urbantech.smartcity360.security.pattern.CompressionDecorator;
import lk.urbantech.smartcity360.security.pattern.DataStream;
import lk.urbantech.smartcity360.security.pattern.EncryptionDecorator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service layer for the security department.
 * Stateless: every call builds a fresh stack, so nothing needs to be synchronised.
 */
public class SecurityService {

    /** Builds the chosen stack, sends the sample batch through it, returns figures for the page. */
    public Map<String, Object> send(boolean auth, boolean compress, boolean encrypt, String apiKey) {
        String reading = buildSampleBatch();
        DataStream stream = buildStack(auth, compress, encrypt, apiKey);
        String output = stream.process(reading);        // may throw SecurityException

        List<String> layers = new ArrayList<>();
        if (auth) layers.add("Auth");
        if (compress) layers.add("Compression");
        if (encrypt) layers.add("Encryption");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("layers", layers.isEmpty() ? "none (plain stream)" : String.join(" + ", layers));
        result.put("originalLength", reading.length());
        result.put("finalLength", output.length());
        result.put("sizeChange", sizeChange(reading.length(), output.length()));
        result.put("preview", output.length() > 160 ? output.substring(0, 160) + "..." : output);
        return result;
    }

    /** Runs compress-then-encrypt and encrypt-then-compress on the same data. */
    public Map<String, Object> compareOrder() {
        String reading = buildSampleBatch();
        int right = new EncryptionDecorator(new CompressionDecorator(new BasicMeterStream()))
                .process(reading).length();
        int wrong = new CompressionDecorator(new EncryptionDecorator(new BasicMeterStream()))
                .process(reading).length();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("original", reading.length());
        result.put("rightOrder", right);
        result.put("wrongOrder", wrong);
        result.put("ratio", String.format(Locale.ROOT, "%.1f", wrong / (double) right));
        return result;
    }

    /** Wraps layers in a fixed, correct order: Auth first, then Compression, then Encryption. */
    private DataStream buildStack(boolean auth, boolean compress, boolean encrypt, String apiKey) {
        DataStream stream = new BasicMeterStream();
        if (auth) stream = new AuthDecorator(stream, apiKey);
        if (compress) stream = new CompressionDecorator(stream);
        if (encrypt) stream = new EncryptionDecorator(stream);
        return stream;
    }

    private String sizeChange(int original, int finalLength) {
        double percent = Math.abs(finalLength - original) * 100.0 / original;
        return String.format(Locale.ROOT, "%.0f%% %s", percent, finalLength <= original ? "smaller" : "larger");
    }

    private String buildSampleBatch() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(String.format(Locale.ROOT, "METER-%d | 2026-11-06 %02d:00 | %.2f kWh\n",
                    101 + i % 5, 8 + i % 12, 3.0 + i * 0.07));
        }
        return sb.toString();
    }
}