package lk.urbantech.smartcity360.policies.pattern;

public class PoliciesDemo {

    public static void main(String[] args) {
        String[] names = {"Night noise", "Loud or truck", "Quiet hours off"};
        String[] rules = {
                "NOISE > 70 AND TIME BETWEEN 22 AND 6",
                "NOISE > 85 OR VEHICLE = TRUCK",
                "NOT TIME BETWEEN 8 AND 18"};

        System.out.println("=== Parsed rules ===");
        Expression[] trees = new Expression[rules.length];
        for (int i = 0; i < rules.length; i++) {
            trees[i] = RuleParser.parse(rules[i]);
            System.out.println(names[i] + ": " + rules[i]);
            System.out.println("    tree: " + trees[i]);
        }

        Context[] situations = {
                new Context(78, 23, "CAR"),
                new Context(78, 12, "CAR"),
                new Context(60, 3, "TRUCK"),
                new Context(90, 15, "CAR")};

        for (Context ctx : situations) {
            System.out.println("\n=== noise " + ctx.getNoise() + ", hour " + ctx.getHour()
                    + ", vehicle " + ctx.getVehicle() + " ===");
            for (int i = 0; i < trees.length; i++) {
                System.out.println("  " + names[i] + ": "
                        + (trees[i].interpret(ctx) ? "TRIGGERED" : "not triggered"));
            }
        }

        System.out.println("\n=== TimeBetween wrap-around (22 to 6) ===");
        Expression night = new TimeBetween(22, 6);
        for (int hour : new int[]{3, 6, 12, 22, 23}) {
            System.out.println("  hour " + hour + " -> " + night.interpret(new Context(0, hour, "CAR")));
        }

        System.out.println("\n=== Precedence: OR binds weaker than AND ===");
        Expression precedence = RuleParser.parse("NOISE > 85 OR VEHICLE = TRUCK AND TIME BETWEEN 22 AND 6");
        System.out.println("  tree: " + precedence);
        System.out.println("  noise 90, hour 12, CAR -> " + precedence.interpret(new Context(90, 12, "CAR")));

        System.out.println("\n=== Malformed rules ===");
        String[] bad = {"NOISE >", "NOISE > abc", "TIME BETWEEN 22 6", "VEHICLE", "HELLO > 5",
                "NOISE > 70 AND", "NOISE > 70 XYZ", "TIME BETWEEN 25 AND 6", ""};
        for (String rule : bad) {
            try {
                RuleParser.parse(rule);
                System.out.println("  '" + rule + "' -> parsed (unexpected!)");
            } catch (IllegalArgumentException e) {
                System.out.println("  '" + rule + "' -> " + e.getMessage());
            }
        }
    }
}