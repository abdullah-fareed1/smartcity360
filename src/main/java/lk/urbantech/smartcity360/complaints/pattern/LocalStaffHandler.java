package lk.urbantech.smartcity360.complaints.pattern;

public class LocalStaffHandler extends ComplaintHandler {

    @Override
    public void handle(Complaint complaint) {
        complaint.addToPath("LocalStaff");
        complaint.addLog("LocalStaff received " + complaint.getId()
                + " (severity " + complaint.getSeverity() + ")");

        if (complaint.getSeverity() >= 1 && complaint.getSeverity() <= 2) {
            complaint.setHandledBy("LocalStaff");
            complaint.addLog("LocalStaff handled it");
        } else {
            complaint.addLog("LocalStaff passed it on");
            passToNext(complaint);
        }
    }
}