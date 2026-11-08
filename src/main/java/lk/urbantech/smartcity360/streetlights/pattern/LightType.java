package lk.urbantech.smartcity360.streetlights.pattern;

/**
 * Flyweight pattern role: FLYWEIGHT.
 * Holds the INTRINSIC (shared) state. Immutable, so one object can be shared by thousands of lights.
 */
public final class LightType {

    private final String model;
    private final int wattage;
    private final String bulbType;
    private final String brightness;

    public LightType(String model, int wattage, String bulbType, String brightness) {
        this.model = model;
        this.wattage = wattage;
        this.bulbType = bulbType;
        this.brightness = brightness;
    }

    public String getModel() { return model; }
    public int getWattage() { return wattage; }
    public String getBulbType() { return bulbType; }
    public String getBrightness() { return brightness; }

    public String describe() {
        return model + " (" + wattage + "W, " + bulbType + ", " + brightness + ")";
    }
}