package lk.urbantech.smartcity360.policies.pattern;

public class NotExpression implements Expression {

    private final Expression inner;

    public NotExpression(Expression inner) {
        this.inner = inner;
    }

    @Override
    public boolean interpret(Context ctx) {
        return !inner.interpret(ctx);
    }

    @Override
    public String toString() {
        return "NOT " + inner;
    }
}