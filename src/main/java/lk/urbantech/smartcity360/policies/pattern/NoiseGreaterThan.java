package lk.urbantech.smartcity360.policies.pattern;

/** Interpreter pattern role: TERMINAL EXPRESSION for "NOISE > n". */
public class NoiseGreaterThan implements Expression {

    private final int limit;

    public NoiseGreaterThan(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean interpret(Context ctx) {
        return ctx.getNoise() > limit;
    }

    @Override
    public String toString() {
        return "NOISE>" + limit;
    }
}