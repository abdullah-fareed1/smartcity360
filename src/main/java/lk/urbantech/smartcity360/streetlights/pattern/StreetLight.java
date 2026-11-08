package lk.urbantech.smartcity360.streetlights.pattern;

/**
 * Flyweight pattern role: CONTEXT.
 * Holds the EXTRINSIC (unique) state and a reference to a shared LightType.
 */
public class StreetLight {

    private final String id;
    private final String location;
    private boolean on;
    private final LightType type;      // shared with many other lights

    public StreetLight(String id, String location, boolean on, LightType type) {
        this.id = id;
        this.location = location;
        this.on = on;
        this.type = type;
    }

    public String getId() { return id; }
    public String getLocation() { return location; }
    public boolean isOn() { return on; }
    public void setOn(boolean on) { this.on = on; }
    public LightType getType() { return type; }

    public String describe() {
        return id + " | " + location + " | " + (on ? "ON" : "OFF") + " | " + type.describe();
    }
}