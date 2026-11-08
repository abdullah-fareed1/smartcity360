package lk.urbantech.smartcity360.policies.pattern;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RuleParser {

    private final List<String> tokens;
    private int pos = 0;

    private RuleParser(String text) {
        // pad > and = so "NOISE>70" works, upper-case everything so keywords are not case-sensitive
        String spaced = text.toUpperCase(Locale.ROOT).replace(">", " > ").replace("=", " = ");
        this.tokens = Arrays.asList(spaced.trim().split("\\s+"));
    }

    /** Parses a whole rule. Throws IllegalArgumentException with a friendly message if it is malformed. */
    public static Expression parse(String ruleText) {
        if (ruleText == null || ruleText.isBlank()) {
            throw new IllegalArgumentException("The rule is empty. Example: NOISE > 70 AND TIME BETWEEN 22 AND 6");
        }
        if (ruleText.length() > 200) {
            throw new IllegalArgumentException("The rule is too long (200 characters maximum).");
        }
        RuleParser parser = new RuleParser(ruleText);
        Expression result = parser.parseOr();
        if (parser.peek() != null) {
            throw new IllegalArgumentException(
                    "Unexpected '" + parser.peek() + "'. Expected AND, OR or the end of the rule.");
        }
        return result;
    }

    // orExpr := andExpr ( OR andExpr )*
    private Expression parseOr() {
        Expression left = parseAnd();
        while ("OR".equals(peek())) {
            next();
            left = new OrExpression(left, parseAnd());
        }
        return left;
    }

    // andExpr := notExpr ( AND notExpr )*
    private Expression parseAnd() {
        Expression left = parseNot();
        while ("AND".equals(peek())) {
            next();
            left = new AndExpression(left, parseNot());
        }
        return left;
    }

    // notExpr := NOT notExpr | atom
    private Expression parseNot() {
        if ("NOT".equals(peek())) {
            next();
            return new NotExpression(parseNot());
        }
        return parseAtom();
    }

    // atom := NOISE > n | TIME BETWEEN n AND n | VEHICLE = word
    private Expression parseAtom() {
        String word = peek();
        if (word == null) {
            throw new IllegalArgumentException("The rule ended unexpectedly. Expected NOISE, TIME or VEHICLE.");
        }
        switch (word) {
            case "NOISE": {
                next();
                expect(">");
                return new NoiseGreaterThan(readNumber("NOISE >"));
            }
            case "TIME": {
                next();
                expect("BETWEEN");
                int from = readNumber("TIME BETWEEN");
                expect("AND");                       // this AND belongs to the condition, not the logic
                int to = readNumber("TIME BETWEEN ... AND");
                if (from > 23 || to > 23) {
                    throw new IllegalArgumentException("Hours must be between 0 and 23.");
                }
                return new TimeBetween(from, to);
            }
            case "VEHICLE": {
                next();
                expect("=");
                String type = peek();
                if (type == null || type.equals("AND") || type.equals("OR") || type.equals("NOT")) {
                    throw new IllegalArgumentException("Expected a vehicle type after VEHICLE =, for example TRUCK.");
                }
                return new VehicleEquals(next());
            }
            default:
                throw new IllegalArgumentException("Unknown condition '" + word
                        + "'. A condition must start with NOISE, TIME or VEHICLE.");
        }
    }

    // ---------- helpers ----------

    /** The current token without consuming it, or null at the end of the rule. */
    private String peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    /** Consumes and returns the current token. */
    private String next() {
        return tokens.get(pos++);
    }

    private void expect(String word) {
        String actual = peek();
        if (!word.equals(actual)) {
            throw new IllegalArgumentException("Expected " + word
                    + (actual == null ? " but the rule ended." : " but found '" + actual + "'."));
        }
        next();
    }

    private int readNumber(String after) {
        String token = peek();
        if (token == null || !token.matches("\\d{1,4}")) {
            throw new IllegalArgumentException("Expected a number after " + after
                    + (token == null ? ", but the rule ended." : ", but found '" + token + "'."));
        }
        return Integer.parseInt(next());
    }
}