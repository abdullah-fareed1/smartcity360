package lk.urbantech.smartcity360.security.pattern;

/**
 * Decorator pattern role: DECORATOR (abstract).
 * Is a DataStream AND has a DataStream, which is what lets layers be stacked.
 */
public abstract class StreamDecorator implements DataStream {

    protected final DataStream wrapped;

    protected StreamDecorator(DataStream wrapped) {
        this.wrapped = wrapped;
    }
}