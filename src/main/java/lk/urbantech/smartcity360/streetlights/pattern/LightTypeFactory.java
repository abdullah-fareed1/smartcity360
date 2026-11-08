package lk.urbantech.smartcity360.streetlights.pattern;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flyweight pattern role: FLYWEIGHT FACTORY.
 * Keeps the pool and returns the existing LightType when one already exists.
 * The pool is static shared state, so it must be thread-safe (ConcurrentHashMap).
 */
public class LightTypeFactory {

    private static final Map<String, LightType> POOL = new ConcurrentHashMap<>();

    private LightTypeFactory() {
    }

    public static LightType getLightType(String model, int wattage, String bulbType, String brightness) {
        String key = model + "|" + wattage + "|" + bulbType + "|" + brightness;
        // atomic: only one thread can create the object for a given key
        return POOL.computeIfAbsent(key, k -> new LightType(model, wattage, bulbType, brightness));
    }

    public static int getPoolSize() {
        return POOL.size();
    }

    /** Empties the pool. Used before a benchmark so the measurement starts clean. */
    public static void clear() {
        POOL.clear();
    }
}