package lk.urbantech.smartcity360.zones.pattern;

public interface CityComponent {

    String getName();

    /** City, District, Street or Building. */
    String getLevel();

    /** Total usage in kWh. A node that is switched off reports 0. */
    double reportUsage();

    void shutdownPower();

    void restorePower();

    /** True if this node (or, for a group, at least one child) has power. */
    boolean isPowerOn();

    /** This node and everything below it as indented text. */
    String describe(int depth);
}