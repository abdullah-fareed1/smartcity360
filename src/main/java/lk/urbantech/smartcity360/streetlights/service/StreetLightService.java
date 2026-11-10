package lk.urbantech.smartcity360.streetlights.service;

import lk.urbantech.smartcity360.streetlights.pattern.LightType;
import lk.urbantech.smartcity360.streetlights.pattern.LightTypeFactory;
import lk.urbantech.smartcity360.streetlights.pattern.StreetLight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service layer for the lighting department.
 * Builds lights with and without the Flyweight and measures the memory difference.
 */
public class StreetLightService {

    private static final int MAX_COUNT = 200_000;
    private static final int RUNS = 5;               // each measurement is repeated, then the median is used

    private static final String[] MODELS = {"LED-Pro-100", "LED-Eco-60", "Sodium-150"};
    private static final int[] WATTS = {100, 60, 150};
    private static final String[] BULBS = {"LED", "LED", "High-pressure sodium"};
    private static final String[] BRIGHTNESS = {
            "Dims to 40% from 23:00 to 05:00", "Constant 70% brightness", "Full brightness all night"};
    private static final String[] STREETS = {"Galle Road", "Duplication Road", "Marine Drive",
            "Independence Avenue", "Bauddhaloka Mawatha", "Janadhipathi Mawatha", "Chatham Street", "Union Place"};

    // ---------- creation ----------

    /** Lights that SHARE their LightType through the factory. */
    public List<StreetLight> createWithFlyweight(int count) {
        List<StreetLight> lights = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int t = i % MODELS.length;
            LightType type = LightTypeFactory.getLightType(MODELS[t], WATTS[t], BULBS[t], BRIGHTNESS[t]);
            lights.add(new StreetLight(lightId(i), STREETS[i % STREETS.length], i % 7 != 0, type));
        }
        return lights;
    }

    /** Lights that each carry their OWN copy of the configuration (the baseline). */
    public List<NonSharedStreetLight> createWithoutFlyweight(int count) {
        List<NonSharedStreetLight> lights = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int t = i % MODELS.length;
            lights.add(new NonSharedStreetLight(lightId(i), STREETS[i % STREETS.length], i % 7 != 0,
                    copyOf(MODELS[t]), WATTS[t], copyOf(BULBS[t]), copyOf(BRIGHTNESS[t])));
        }
        return lights;
    }

    /** A few lights for the preview table. */
    public List<StreetLight> sample(int n) {
        return createWithFlyweight(n);
    }

    // ---------- benchmark ----------

    /**
     * synchronized so two users cannot distort each other's measurement.
     * Memory readings from the JVM are noisy (garbage collection can run at any moment), so the
     * measurement is repeated RUNS times, impossible readings are thrown away, and the MEDIAN is reported.
     */
    public synchronized Map<String, Object> runBenchmark(String countText) {
        int count = parseCount(countText);

        List<Long> flyReadings = new ArrayList<>();
        List<Long> plainReadings = new ArrayList<>();
        int poolSize = 0;
        for (int i = 0; i < RUNS; i++) {
            long fly = measureWithFlyweight(count);
            poolSize = LightTypeFactory.getPoolSize();
            long plain = measureWithoutFlyweight(count);
            if (fly > 0) flyReadings.add(fly);          // a reading <= 0 means GC ran during measuring
            if (plain > 0) plainReadings.add(plain);
        }
        if (flyReadings.isEmpty() || plainReadings.isEmpty()) {
            throw new IllegalArgumentException("The memory readings were unstable on this JVM. Please run the benchmark again.");
        }

        long flyBytes = median(flyReadings);
        long plainBytes = median(plainReadings);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", count);
        result.put("poolSize", poolSize);
        result.put("flyMb", flyBytes / (1024.0 * 1024.0));
        result.put("plainMb", plainBytes / (1024.0 * 1024.0));
        result.put("flyPerLight", flyBytes / count);
        result.put("plainPerLight", plainBytes / count);
        result.put("savedPercent", (plainBytes - flyBytes) * 100.0 / plainBytes);
        result.put("runsUsed", Math.min(flyReadings.size(), plainReadings.size()) + " of " + RUNS);
        result.put("flyRangeMb", range(flyReadings));
        result.put("plainRangeMb", range(plainReadings));
        return result;
    }

    private long median(List<Long> readings) {
        List<Long> sorted = new ArrayList<>(readings);
        Collections.sort(sorted);
        return sorted.get(sorted.size() / 2);
    }

    private String range(List<Long> readings) {
        long min = Collections.min(readings);
        long max = Collections.max(readings);
        return String.format(Locale.ROOT, "%.2f to %.2f MB", min / (1024.0 * 1024.0), max / (1024.0 * 1024.0));
    }

    private long measureWithFlyweight(int count) {
        LightTypeFactory.clear();                 // start with an empty pool
        long before = usedMemory();
        List<StreetLight> lights = createWithFlyweight(count);
        long after = usedMemory();
        if (lights.size() != count) {             // also keeps the list reachable until here
            throw new IllegalStateException("Unexpected light count");
        }
        return after - before;      // may be <= 0 if GC ran: runBenchmark discards those
    }

    private long measureWithoutFlyweight(int count) {
        long before = usedMemory();
        List<NonSharedStreetLight> lights = createWithoutFlyweight(count);
        long after = usedMemory();
        if (lights.size() != count) {
            throw new IllegalStateException("Unexpected light count");
        }
        return after - before;      // may be <= 0 if GC ran: runBenchmark discards those
    }

    // ---------- helpers ----------

    private long usedMemory() {
        Runtime rt = Runtime.getRuntime();
        System.gc();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.gc();
        return rt.totalMemory() - rt.freeMemory();
    }

    private int parseCount(String text) {
        String cleaned = (text == null) ? "" : text.trim().replace(",", "");   // accepts "50,000" too
        int count;
        try {
            count = Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a whole number of streetlights, for example 25000.");
        }
        if (count < 1 || count > MAX_COUNT) {
            throw new IllegalArgumentException("Please enter a number between 1 and "
                    + String.format("%,d", MAX_COUNT) + ".");
        }
        return count;
    }
    /** Forces a REAL copy. new String(String) would reuse the same byte array on modern JVMs. */
    private String copyOf(String s) {
        return new String(s.toCharArray());
    }

    private String lightId(int i) {
        return String.format(Locale.ROOT, "SL-%05d", i + 1);
    }
}