package bto.Model;

import java.time.LocalDateTime;

public class Application {
    private String id;
    private String projectName;
    private String applicantNric;
    private String flatType;
    private ApplicationStatus status;
    private LocalDateTime createdTime;
    private boolean withdrawalRequested;

    public Application(String projectName, String applicantNric, String flatType) {
        this.projectName = projectName;
        this.applicantNric = applicantNric;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;
        this.createdTime = LocalDateTime.now();
    }

        // Getters
    public String getId() { return id; }
    public String getApplicantNric() { return applicantNric; }
    public String getProjectName() { return projectName; }
    public String getFlatType() { return flatType; }
    public ApplicationStatus getStatus() { return status; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public boolean getWithdrawalRequested() { return withdrawalRequested; }

    // Setters
    public Application setStatus(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    public Application setId(String string) {
        this.id = string;
        return this;
    }

    public Application setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setWithdrawalRequested(boolean requested) { this.withdrawalRequested = requested; }
}