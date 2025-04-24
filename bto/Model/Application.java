package bto.Model;

import java.time.LocalDateTime;

/**
 * Represents an application submitted by an applicant for a housing project.
 */
public class Application {
    private String id;
    private String projectName;
    private String applicantNric;
    private String flatType;
    private ApplicationStatus status;
    private LocalDateTime createdTime;
    private boolean withdrawalRequested;

    /**
     * Constructs an Application with the specified project name, applicant NRIC, and flat type.
     *
     * @param projectName The name of the project the application is for.
     * @param applicantNric The NRIC of the applicant submitting the application.
     * @param flatType The type of flat the applicant is applying for.
     */
    public Application(String projectName, String applicantNric, String flatType) {
        this.projectName = projectName;
        this.applicantNric = applicantNric;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;
        this.createdTime = LocalDateTime.now();
    }

    /**
     * Gets the ID of the application.
     *
     * @return The application ID.
     */
    public String getId() { return id; }

    /**
     * Gets the NRIC of the applicant who submitted the application.
     *
     * @return The applicant's NRIC.
     */
    public String getApplicantNric() { return applicantNric; }

    /**
     * Gets the name of the project the application is for.
     *
     * @return The project name.
     */
    public String getProjectName() { return projectName; }

    /**
     * Gets the type of flat the applicant is applying for.
     *
     * @return The flat type.
     */
    public String getFlatType() { return flatType; }

    /**
     * Gets the current status of the application.
     *
     * @return The application status.
     */
    public ApplicationStatus getStatus() { return status; }

    /**
     * Gets the time the application was created.
     *
     * @return The creation time of the application.
     */
    public LocalDateTime getCreatedTime() { return createdTime; }

    /**
     * Checks if a withdrawal has been requested for the application.
     *
     * @return {@code true} if a withdrawal has been requested, {@code false} otherwise.
     */
    public boolean getWithdrawalRequested() { return withdrawalRequested; }

    /**
     * Sets the status of the application.
     *
     * @param status The new status of the application.
     * @return The updated Application object.
     */
    public Application setStatus(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Sets the ID of the application.
     *
     * @param id The new application ID.
     * @return The updated Application object.
     */
    public Application setId(String string) {
        this.id = string;
        return this;
    }

    /**
     * Sets the creation time of the application.
     *
     * @param createdTime The new creation time.
     * @return The updated Application object.
     */
    public Application setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * Sets whether a withdrawal has been requested for the application.
     *
     * @param requested {@code true} if a withdrawal has been requested, {@code false} otherwise.
     */
    public void setWithdrawalRequested(boolean requested) { this.withdrawalRequested = requested; }
}