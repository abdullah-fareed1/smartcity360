package lk.urbantech.smartcity360.streetlights.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Console demo for the Flyweight pattern (pure Java). */
public class StreetLightDemo {

    public static void main(String[] args) {
        String[] models = {"LED-Pro-100", "LED-Eco-60", "Sodium-150"};
        int[] watts = {100, 60, 150};
        String[] bulbs = {"LED", "LED", "High-pressure sodium"};
        String[] brightness = {"Dims to 40% from 23:00 to 05:00", "Constant 70% brightness", "Full brightness all night"};
        String[] streets = {"Galle Road", "Duplication Road", "Marine Drive", "Independence Avenue",
                "Bauddhaloka Mawatha", "Janadhipathi Mawatha", "Chatham Street", "Union Place"};

        List<StreetLight> lights = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            int t = i % models.length;
            LightType type = LightTypeFactory.getLightType(models[t], watts[t], bulbs[t], brightness[t]);
            lights.add(new StreetLight(String.format(Locale.ROOT, "SL-%05d", i + 1),
                    streets[i % streets.length], true, type));
        }

        System.out.println("StreetLight objects created: " + lights.size());
        System.out.println("LightType objects in pool:   " + LightTypeFactory.getPoolSize());
        System.out.println("Light 1 and light 4 share the same LightType object: "
                + (lights.get(0).getType() == lights.get(3).getType()));
        System.out.println("Light 1 and light 2 share the same LightType object: "
                + (lights.get(0).getType() == lights.get(1).getType()));
        System.out.println();
        System.out.println(lights.get(0).describe());
        System.out.println(lights.get(1).describe());
        System.out.println(lights.get(9_999).describe());
    }
}