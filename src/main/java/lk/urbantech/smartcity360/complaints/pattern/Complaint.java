package lk.urbantech.smartcity360.complaints.pattern;

import java.util.ArrayList;
import java.util.List;

public class Complaint {

    private String id;
    private String citizen;
    private String description;
    private int severity;
    private String handledBy;                          // null until someone handles it
    private List<String> path = new ArrayList<>();     // handlers that saw it, in order
    private List<String> log = new ArrayList<>();      // received / handled / passed on events

    public Complaint() {
    }

    public Complaint(String id, String citizen, String description, int severity) {
        this.id = id;
        this.citizen = citizen;
        this.description = description;
        this.severity = severity;
    }

    public void addToPath(String handlerName) {
        path.add(handlerName);
    }

    public void addLog(String line) {
        log.add(line);
    }

    public boolean isHandled() {
        return handledBy != null;
    }

    /** e.g. "LocalStaff, Supervisor" */
    public String getPathText() {
        return String.join(", ", path);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCitizen() { return citizen; }
    public void setCitizen(String citizen) { this.citizen = citizen; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getSeverity() { return severity; }
    public void setSeverity(int severity) { this.severity = severity; }

    public String getHandledBy() { return handledBy; }
    public void setHandledBy(String handledBy) { this.handledBy = handledBy; }

    public List<String> getPath() { return path; }
    public List<String> getLog() { return log; }
}