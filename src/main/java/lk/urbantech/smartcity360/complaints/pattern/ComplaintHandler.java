package lk.urbantech.smartcity360.complaints.pattern;

public abstract class ComplaintHandler {

    protected ComplaintHandler next;

    /** Returns the handler that was passed in, so calls can be chained. */
    public ComplaintHandler setNext(ComplaintHandler next) {
        this.next = next;
        return next;
    }

    /** Each concrete handler decides: handle it, or pass it on. */
    public abstract void handle(Complaint complaint);

    protected void passToNext(Complaint complaint) {
        if (next == null) {
            complaint.addLog("End of chain reached: nobody could handle this complaint");
        } else {
            next.handle(complaint);
        }
    }
}