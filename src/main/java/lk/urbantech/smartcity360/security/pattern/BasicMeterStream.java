package lk.urbantech.smartcity360.security.pattern;

/**
 * Decorator pattern role: CONCRETE COMPONENT.
 * The core stream. Never edited when new layers are added.
 */
public class BasicMeterStream implements DataStream {

    @Override
    public String process(String data) {
        return data;
    }
}