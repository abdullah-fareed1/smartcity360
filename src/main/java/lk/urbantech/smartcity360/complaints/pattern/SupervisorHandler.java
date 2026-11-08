package lk.urbantech.smartcity360.complaints.pattern;

public class SupervisorHandler extends ComplaintHandler {

    @Override
    public void handle(Complaint complaint) {
        complaint.addToPath("Supervisor");
        complaint.addLog("Supervisor received " + complaint.getId()
                + " (severity " + complaint.getSeverity() + ")");

        if (complaint.getSeverity() >= 3 && complaint.getSeverity() <= 4) {
            complaint.setHandledBy("Supervisor");
            complaint.addLog("Supervisor handled it");
        } else {
            complaint.addLog("Supervisor passed it on");
            passToNext(complaint);
        }
    }
}