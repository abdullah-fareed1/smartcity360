package lk.urbantech.smartcity360.zones.pattern;

public class Building implements CityComponent {

    private final String name;
    private final double usageKwh;
    private boolean powerOn = true;

    public Building(String name, double usageKwh) {
        this.name = name;
        this.usageKwh = usageKwh;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return "Building";
    }

    @Override
    public double reportUsage() {
        return powerOn ? usageKwh : 0;
    }

    @Override
    public void shutdownPower() {
        powerOn = false;
    }

    @Override
    public void restorePower() {
        powerOn = true;
    }

    @Override
    public boolean isPowerOn() {
        return powerOn;
    }

    @Override
    public String describe(int depth) {
        return "  ".repeat(depth) + name + " (" + getLevel() + ") - "
                + reportUsage() + " kWh - " + (powerOn ? "ON" : "OFF") + "\n";
    }
}