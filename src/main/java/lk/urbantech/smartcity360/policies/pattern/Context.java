package lk.urbantech.smartcity360.policies.pattern;

public class Context {

    private int noise;          // decibels
    private int hour;           // 0-23
    private String vehicle;     // e.g. CAR, TRUCK

    public Context() {
    }

    public Context(int noise, int hour, String vehicle) {
        this.noise = noise;
        this.hour = hour;
        this.vehicle = vehicle;
    }

    public int getNoise() { return noise; }
    public void setNoise(int noise) { this.noise = noise; }

    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }

    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }
}