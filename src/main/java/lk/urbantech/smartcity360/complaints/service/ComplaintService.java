package lk.urbantech.smartcity360.complaints.service;

import lk.urbantech.smartcity360.complaints.pattern.CityManagerHandler;
import lk.urbantech.smartcity360.complaints.pattern.Complaint;
import lk.urbantech.smartcity360.complaints.pattern.ComplaintHandler;
import lk.urbantech.smartcity360.complaints.pattern.LocalStaffHandler;
import lk.urbantech.smartcity360.complaints.pattern.SupervisorHandler;

import java.util.ArrayList;
import java.util.List;

public class ComplaintService {

    private final ComplaintHandler first;                         // head of the chain
    private final List<Complaint> complaints = new ArrayList<>();
    private int nextNumber;

    public ComplaintService() {
        ComplaintHandler local = new LocalStaffHandler();
        local.setNext(new SupervisorHandler()).setNext(new CityManagerHandler());
        first = local;
        reset();
    }

    /** Clears everything and re-adds the four sample complaints. */
    public synchronized void reset() {
        complaints.clear();
        nextNumber = 1;
        submit("Nimal Perera", "Pothole on Galle Road", "2");
        submit("Kumari Silva", "Burst water main on Duplication Road", "4");
        submit("Ruwan Fernando", "Power outage across Colombo 03", "5");
        submit("Anura Jayasinghe", "Major flooding across several districts", "6");
    }

    /** Validates, creates the complaint, sends it down the chain, stores it. */
    public synchronized Complaint submit(String citizen, String description, String severityText) {
        if (citizen == null || citizen.isBlank()) {
            throw new IllegalArgumentException("Please enter the citizen's name.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Please describe the complaint.");
        }
        if (description.trim().length() > 200) {
            throw new IllegalArgumentException("Description is too long (200 characters maximum).");
        }

        int severity;
        try {
            severity = Integer.parseInt(severityText == null ? "" : severityText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Severity must be a whole number such as 1, 2, 3...");
        }
        if (severity < 1) {
            throw new IllegalArgumentException("Severity must be 1 or higher.");
        }

        String id = String.format("C-%03d", nextNumber++);
        Complaint complaint = new Complaint(id, citizen.trim(), description.trim(), severity);

        first.handle(complaint);          // the service only knows the first handler
        complaints.add(complaint);
        return complaint;
    }

    public synchronized List<Complaint> getAll() {
        return new ArrayList<>(complaints);
    }
}