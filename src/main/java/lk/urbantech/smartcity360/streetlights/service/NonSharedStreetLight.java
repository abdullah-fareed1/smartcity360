package lk.urbantech.smartcity360.streetlights.service;

/**
 * NOT part of the Flyweight pattern. A baseline used only to measure memory.
 * Every light carries its own full copy of the configuration.
 */
public class NonSharedStreetLight {

    private final String id;
    private final String location;
    private boolean on;
    private final String model;
    private final int wattage;
    private final String bulbType;
    private final String brightness;

    public NonSharedStreetLight(String id, String location, boolean on,
                                String model, int wattage, String bulbType, String brightness) {
        this.id = id;
        this.location = location;
        this.on = on;
        this.model = model;
        this.wattage = wattage;
        this.bulbType = bulbType;
        this.brightness = brightness;
    }

    public String getId() { return id; }
    public String getLocation() { return location; }
    public boolean isOn() { return on; }
    public String getModel() { return model; }
    public int getWattage() { return wattage; }
    public String getBulbType() { return bulbType; }
    public String getBrightness() { return brightness; }
}