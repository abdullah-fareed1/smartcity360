package lk.urbantech.smartcity360.zones.service;

import lk.urbantech.smartcity360.zones.pattern.Building;
import lk.urbantech.smartcity360.zones.pattern.CityComponent;
import lk.urbantech.smartcity360.zones.pattern.ZoneGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZoneService {

    private CityComponent city;
    private Map<String, CityComponent> index;

    public ZoneService() {
        reset();
    }

    public synchronized void reset() {
        index = new LinkedHashMap<>();
        city = buildSampleCity();
    }

    public synchronized List<String> getNodeNames() {
        return new ArrayList<>(index.keySet());
    }

    public synchronized String getTreeText() {
        return city.describe(0);
    }

    public synchronized String shutdown(String name) {
        CityComponent node = find(name);
        node.shutdownPower();
        return node.getName() + " switched OFF. City usage is now " + fmt(city.reportUsage()) + " kWh.";
    }

    public synchronized String restore(String name) {
        CityComponent node = find(name);
        node.restorePower();
        return node.getName() + " switched ON. City usage is now " + fmt(city.reportUsage()) + " kWh.";
    }

    public synchronized double usageOf(String name) {
        return find(name).reportUsage();
    }

    public synchronized String report(String name) {
        return name + " uses " + fmt(usageOf(name)) + " kWh.";
    }


    private CityComponent find(String name) {
        CityComponent node = (name == null) ? null : index.get(name);
        if (node == null) {
            throw new IllegalArgumentException("Unknown zone: " + name);
        }
        return node;
    }

    private String fmt(double value) {
        return String.format("%.1f", value);
    }

    private CityComponent buildSampleCity() {
        ZoneGroup colombo = register(new ZoneGroup("Colombo City", "City"));

        // ---- Colombo 03 ----
        ZoneGroup colombo03 = register(new ZoneGroup("Colombo 03", "District"));
        ZoneGroup galleRoad = register(new ZoneGroup("Galle Road", "Street"));
        galleRoad.add(register(new Building("Building A", 120)));
        galleRoad.add(register(new Building("Building B", 85)));
        galleRoad.add(register(new Building("Seaside Tower", 210)));
        galleRoad.add(register(new Building("Harbour Mall", 340)));

        ZoneGroup duplicationRoad = register(new ZoneGroup("Duplication Road", "Street"));
        duplicationRoad.add(register(new Building("Building C", 60)));
        duplicationRoad.add(register(new Building("Green Apartments", 95)));
        duplicationRoad.add(register(new Building("Lakeview Hotel", 280)));

        ZoneGroup marineDrive = register(new ZoneGroup("Marine Drive", "Street"));
        marineDrive.add(register(new Building("Sunset Residency", 150)));
        marineDrive.add(register(new Building("Coastal Offices", 175)));

        colombo03.add(galleRoad);
        colombo03.add(duplicationRoad);
        colombo03.add(marineDrive);

        // ---- Colombo 07 ----
        ZoneGroup colombo07 = register(new ZoneGroup("Colombo 07", "District"));
        ZoneGroup independenceAve = register(new ZoneGroup("Independence Avenue", "Street"));
        independenceAve.add(register(new Building("Town Hall", 230)));
        independenceAve.add(register(new Building("Public Library", 90)));
        independenceAve.add(register(new Building("National Museum", 110)));

        ZoneGroup bauddhaloka = register(new ZoneGroup("Bauddhaloka Mawatha", "Street"));
        bauddhaloka.add(register(new Building("University Block", 260)));
        bauddhaloka.add(register(new Building("Student Hostel", 140)));

        colombo07.add(independenceAve);
        colombo07.add(bauddhaloka);

        // ---- Fort ----
        ZoneGroup fort = register(new ZoneGroup("Fort", "District"));
        ZoneGroup janadhipathi = register(new ZoneGroup("Janadhipathi Mawatha", "Street"));
        janadhipathi.add(register(new Building("Treasury Office", 310)));
        janadhipathi.add(register(new Building("Stock Exchange", 270)));
        janadhipathi.add(register(new Building("Fort Hotel", 200)));

        ZoneGroup chatham = register(new ZoneGroup("Chatham Street", "Street"));
        chatham.add(register(new Building("Trade Centre", 190)));
        chatham.add(register(new Building("Customs House", 160)));
        chatham.add(register(new Building("Fort Cafe", 45)));

        fort.add(janadhipathi);
        fort.add(chatham);

        colombo.add(colombo03);
        colombo.add(colombo07);
        colombo.add(fort);
        return colombo;
    }

    private <T extends CityComponent> T register(T node) {
        index.put(node.getName(), node);
        return node;
    }
}