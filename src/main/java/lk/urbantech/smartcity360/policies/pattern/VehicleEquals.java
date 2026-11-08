package lk.urbantech.smartcity360.policies.pattern;

public class VehicleEquals implements Expression {

    private final String vehicle;

    public VehicleEquals(String vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean interpret(Context ctx) {
        return vehicle.equalsIgnoreCase(ctx.getVehicle());
    }

    @Override
    public String toString() {
        return "VEHICLE=" + vehicle;
    }
}