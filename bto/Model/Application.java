package bto.Model;

public class Application {
    private String id;
    private String projectName;
    private String applicantNric;
    private String flatType;
    private ApplicationStatus status;

    public Application(String projectName, String applicantNric, String flatType) {
        this.projectName = projectName;
        this.applicantNric = applicantNric;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;
    }

        // Getters
    public String getId() { return id; }
    public String getApplicantNric() { return applicantNric; }
    public String getProjectName() { return projectName; }
    public String getFlatType() { return flatType; }
    public ApplicationStatus getStatus() { return status; }


    // Setters
    public Application setStatus(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    public Application setId(String string) {
        this.id = string;
        return this;
    }
}