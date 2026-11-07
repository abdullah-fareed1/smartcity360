package lk.urbantech.smartcity360.zones.pattern;

import java.util.ArrayList;
import java.util.List;

public class ZoneGroup implements CityComponent {

    private final String name;
    private final String level;
    private final List<CityComponent> children = new ArrayList<>();

    public ZoneGroup(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public void add(CityComponent child) {
        children.add(child);
    }

    public void remove(CityComponent child) {
        children.remove(child);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public double reportUsage() {
        double total = 0;
        for (CityComponent child : children) {
            total += child.reportUsage();   // recursion: a child may itself be a group
        }
        return total;
    }

    @Override
    public void shutdownPower() {
        for (CityComponent child : children) {
            child.shutdownPower();
        }
    }

    @Override
    public void restorePower() {
        for (CityComponent child : children) {
            child.restorePower();
        }
    }

    @Override
    public boolean isPowerOn() {
        for (CityComponent child : children) {
            if (child.isPowerOn()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String describe(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(depth)).append(name).append(" (").append(level).append(") - ")
                .append(reportUsage()).append(" kWh - ").append(isPowerOn() ? "ON" : "OFF").append("\n");
        for (CityComponent child : children) {
            sb.append(child.describe(depth + 1));
        }
        return sb.toString();
    }
}