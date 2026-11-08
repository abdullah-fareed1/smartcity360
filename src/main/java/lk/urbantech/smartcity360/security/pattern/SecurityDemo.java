package lk.urbantech.smartcity360.security.pattern;

import java.util.Locale;

/** Console demo for the Decorator pattern (pure Java). */
public class SecurityDemo {

    public static void main(String[] args) {
        String data = sampleBatch();
        System.out.println("Original payload: " + data.length() + " characters\n");

        // Stack 1: all three layers, valid key
        DataStream full = new EncryptionDecorator(
                new CompressionDecorator(
                        new AuthDecorator(new BasicMeterStream(), "UT-SECRET-2026")));
        show("Stack 1: Auth + Compression + Encryption", full, data);

        // Stack 2: different layers, same core class. Proves flexibility.
        DataStream authEnc = new EncryptionDecorator(
                new AuthDecorator(new BasicMeterStream(), "UT-SECRET-2026"));
        show("Stack 2: Auth + Encryption only", authEnc, data);

        // Stack 3: wrong key
        DataStream badKey = new CompressionDecorator(
                new AuthDecorator(new BasicMeterStream(), "WRONG-KEY"));
        show("Stack 3: wrong API key", badKey, data);

        // Order matters: compress then encrypt, versus encrypt then compress
        DataStream rightOrder = new EncryptionDecorator(new CompressionDecorator(new BasicMeterStream()));
        DataStream wrongOrder = new CompressionDecorator(new EncryptionDecorator(new BasicMeterStream()));
        System.out.println("=== Order comparison ===");
        System.out.println("Compress then encrypt: " + rightOrder.process(data).length() + " characters");
        System.out.println("Encrypt then compress: " + wrongOrder.process(data).length() + " characters");
    }

    private static void show(String label, DataStream stream, String data) {
        System.out.println("=== " + label + " ===");
        try {
            String out = stream.process(data);
            System.out.println("Final length: " + out.length() + " characters");
            System.out.println("Starts with:  " + out.substring(0, Math.min(60, out.length())) + "...\n");
        } catch (SecurityException e) {
            System.out.println("Rejected: " + e.getMessage() + "\n");
        }
    }

    // Same sample data as SecurityService (a pattern class cannot import the service).
    private static String sampleBatch() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(String.format(Locale.ROOT, "METER-%d | 2026-11-06 %02d:00 | %.2f kWh\n",
                    101 + i % 5, 8 + i % 12, 3.0 + i * 0.07));
        }
        return sb.toString();
    }
}