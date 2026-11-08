package lk.urbantech.smartcity360.policies.pattern;


public class TimeBetween implements Expression {

    private final int from;
    private final int to;

    public TimeBetween(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean interpret(Context ctx) {
        int hour = ctx.getHour();
        if (from <= to) {
            return hour >= from && hour < to;        // normal range, e.g. 8 to 18
        }
        return hour >= from || hour < to;            // wraps midnight, e.g. 22 to 6
    }

    @Override
    public String toString() {
        return "TIME " + from + "-" + to;
    }
}