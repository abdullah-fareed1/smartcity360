package lk.urbantech.smartcity360.complaints.pattern;

public class CityManagerHandler extends ComplaintHandler {

    @Override
    public void handle(Complaint complaint) {
        complaint.addToPath("CityManager");
        complaint.addLog("CityManager received " + complaint.getId()
                + " (severity " + complaint.getSeverity() + ")");

        if (complaint.getSeverity() == 5) {
            complaint.setHandledBy("CityManager");
            complaint.addLog("CityManager handled it");
        } else {
            complaint.addLog("CityManager passed it on");
            passToNext(complaint);       // next is null, so this logs the end-of-chain message
        }
    }
}