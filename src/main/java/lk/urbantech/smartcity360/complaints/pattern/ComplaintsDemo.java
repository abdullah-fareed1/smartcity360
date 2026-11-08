package lk.urbantech.smartcity360.complaints.pattern;

public class ComplaintsDemo {

    public static void main(String[] args) {
        // Wire the chain: local -> supervisor -> manager
        ComplaintHandler local = new LocalStaffHandler();
        local.setNext(new SupervisorHandler()).setNext(new CityManagerHandler());

        Complaint[] complaints = {
                new Complaint("C-001", "Nimal Perera", "Pothole on Galle Road", 2),
                new Complaint("C-002", "Kumari Silva", "Burst water main on Duplication Road", 4),
                new Complaint("C-003", "Ruwan Fernando", "Power outage across Colombo 03", 5),
                new Complaint("C-004", "Anura Jayasinghe", "Major flooding across several districts", 6)
        };

        for (Complaint c : complaints) {
            local.handle(c);                     // the client only knows the FIRST handler
            System.out.println("=== " + c.getId() + " (severity " + c.getSeverity() + ") ===");
            c.getLog().forEach(line -> System.out.println("  " + line));
            System.out.println("  Path: " + c.getPathText());
            System.out.println("  Result: " + (c.isHandled() ? "handled by " + c.getHandledBy() : "UNHANDLED"));
            System.out.println();
        }
    }
}