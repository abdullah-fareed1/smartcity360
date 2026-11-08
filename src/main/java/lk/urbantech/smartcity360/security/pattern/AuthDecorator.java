package lk.urbantech.smartcity360.security.pattern;

/** Decorator pattern role: CONCRETE DECORATOR. Rejects senders with a wrong API key. */
public class AuthDecorator extends StreamDecorator {

    // Demo only: a real system would never hard-code a key.
    private static final String VALID_KEY = "UT-SECRET-2026";

    private final String apiKey;

    public AuthDecorator(DataStream wrapped, String apiKey) {
        super(wrapped);
        this.apiKey = apiKey;
    }

    @Override
    public String process(String data) {
        if (!VALID_KEY.equals(apiKey)) {
            throw new SecurityException("Invalid API key");
        }
        return wrapped.process(data);
    }
}