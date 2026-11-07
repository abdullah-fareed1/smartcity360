package lk.urbantech.smartcity360.zones.pattern;

public class ZonesDemo {

    public static void main(String[] args) {

        ZoneGroup galleRoad = new ZoneGroup("Galle Road", "Street");
        galleRoad.add(new Building("Building A", 120));
        galleRoad.add(new Building("Building B", 85));

        Building buildingC = new Building("Building C", 60);
        ZoneGroup duplicationRoad = new ZoneGroup("Duplication Road", "Street");
        duplicationRoad.add(buildingC);

        ZoneGroup colombo03 = new ZoneGroup("Colombo 03", "District");
        colombo03.add(galleRoad);
        colombo03.add(duplicationRoad);

        ZoneGroup city = new ZoneGroup("Colombo City", "City");
        city.add(colombo03);

        System.out.println("=== Initial tree ===");
        System.out.print(city.describe(0));

        System.out.println("\n=== Same command, different levels ===");
        printUsage(buildingC);
        printUsage(galleRoad);
        printUsage(city);

        System.out.println("\n=== Shutdown Duplication Road ===");
        duplicationRoad.shutdownPower();
        printUsage(city);
        System.out.print(city.describe(0));

        System.out.println("\n=== Shutdown the whole city ===");
        city.shutdownPower();
        printUsage(city);

        System.out.println("\n=== Restore the whole city ===");
        city.restorePower();
        printUsage(city);
    }

    // Takes the interface type, so it works for a building, a street or the city.
    private static void printUsage(CityComponent node) {
        System.out.println(node.getName() + " uses " + node.reportUsage() + " kWh");
    }
}