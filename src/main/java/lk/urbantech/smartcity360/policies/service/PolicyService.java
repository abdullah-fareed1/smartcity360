package lk.urbantech.smartcity360.policies.service;

import lk.urbantech.smartcity360.policies.pattern.Context;
import lk.urbantech.smartcity360.policies.pattern.Expression;
import lk.urbantech.smartcity360.policies.pattern.RuleParser;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class PolicyService {

    private final Map<String, String> rules = new LinkedHashMap<>();

    public PolicyService() {
        reset();
    }

    public synchronized void reset() {
        rules.clear();
        rules.put("Night noise", "NOISE > 70 AND TIME BETWEEN 22 AND 6");
        rules.put("Loud or truck", "NOISE > 85 OR VEHICLE = TRUCK");
        rules.put("Quiet hours off", "NOT TIME BETWEEN 8 AND 18");
    }

    /** Returns a copy so callers cannot change the internal map. */
    public synchronized Map<String, String> getRules() {
        return new LinkedHashMap<>(rules);
    }

    /** Parses the rule FIRST, so an invalid rule can never be saved. */
    public synchronized void addRule(String name, String text) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please give the rule a name.");
        }
        String cleanName = name.trim();
        if (cleanName.length() > 40) {
            throw new IllegalArgumentException("The rule name is too long (40 characters maximum).");
        }
        if (rules.containsKey(cleanName)) {
            throw new IllegalArgumentException("A rule named '" + cleanName + "' already exists. Remove it first.");
        }
        RuleParser.parse(text);                       // throws a friendly error if malformed
        rules.put(cleanName, text.trim());
    }

    public synchronized void removeRule(String name) {
        if (name == null || rules.remove(name) == null) {
            throw new IllegalArgumentException("Unknown rule: " + name);
        }
    }

    /** Parses one rule text and checks it against the situation typed on the page. */
    public Map<String, Object> evaluate(String ruleText, String noiseText, String hourText, String vehicle) {
        Expression rule = RuleParser.parse(ruleText);
        Context ctx = buildContext(noiseText, hourText, vehicle);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tree", rule.toString());
        result.put("situation", "noise " + ctx.getNoise() + ", hour " + ctx.getHour() + ", vehicle " + ctx.getVehicle());
        result.put("triggered", rule.interpret(ctx));
        return result;
    }

    /** Checks every saved rule against the situation. */
    public synchronized Map<String, Boolean> checkAll(String noiseText, String hourText, String vehicle) {
        Context ctx = buildContext(noiseText, hourText, vehicle);
        Map<String, Boolean> results = new LinkedHashMap<>();
        for (Map.Entry<String, String> rule : rules.entrySet()) {
            results.put(rule.getKey(), RuleParser.parse(rule.getValue()).interpret(ctx));
        }
        return results;
    }

    // ---------- helpers ----------

    private Context buildContext(String noiseText, String hourText, String vehicle) {
        int noise = readWholeNumber(noiseText, 0, 200, "Noise must be a whole number between 0 and 200.");
        int hour = readWholeNumber(hourText, 0, 23, "Hour must be a whole number between 0 and 23.");
        if (vehicle == null || vehicle.isBlank()) {
            throw new IllegalArgumentException("Please choose a vehicle.");
        }
        return new Context(noise, hour, vehicle.trim().toUpperCase(Locale.ROOT));
    }

    private int readWholeNumber(String text, int min, int max, String errorMessage) {
        try {
            int value = Integer.parseInt(text == null ? "" : text.trim());
            if (value >= min && value <= max) {
                return value;
            }
        } catch (NumberFormatException e) {
            // falls through to the friendly message below
        }
        throw new IllegalArgumentException(errorMessage);
    }
}