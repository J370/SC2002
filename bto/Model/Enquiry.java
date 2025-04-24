package bto.Model;

import java.time.LocalDateTime;

/**
 * Represents an enquiry submitted by an applicant for a specific project.
 * An enquiry can be replied to by an officer.
 */
public class Enquiry {
    private int id;
    private final String applicantNric;
    private final String projectName;
    private String enquiryDetails;
    private final LocalDateTime createdTime;
    private String reply;
    private String repliedBy;
    private LocalDateTime repliedTime;

    /**
     * Constructs an Enquiry with the specified details.
     *
     * @param id The ID of the enquiry.
     * @param applicantNric The NRIC of the applicant who submitted the enquiry.
     * @param projectName The name of the project the enquiry is about.
     * @param enquiryDetails The details of the enquiry.
     * @param createdTime The time the enquiry was created.
     * @param reply The reply to the enquiry (if any).
     * @param repliedBy The name of the officer who replied to the enquiry (if any).
     * @param repliedTime The time the enquiry was replied to (if any).
     */
    public Enquiry(int id, String applicantNric, String projectName, String enquiryDetails, LocalDateTime createdTime, String reply, String repliedBy, LocalDateTime repliedTime) {
        this.id = id;
        this.applicantNric = applicantNric;
        this.projectName = projectName;
        this.enquiryDetails = enquiryDetails;
        this.createdTime = createdTime;
        this.reply = reply;
        this.repliedBy = repliedBy;
        this.repliedTime = repliedTime;
    }

    /**
     * Sets the reply to the enquiry.
     *
     * @param reply The reply content.
     * @param officerName The name of the officer replying to the enquiry.
     */
    public void setReply(String reply, String officerName) {
        if (reply == null) {
            System.err.printf("Reply cannot be empty");
            return;
        }
        if (this.reply != null) {
            System.err.printf("Enquiry already has a reply");
            return;
        }

        this.reply = reply;
        this.repliedBy = officerName;
        this.repliedTime = LocalDateTime.now();
    }

    /**
     * Edits the details of the enquiry.
     *
     * @param enquiryDetails The new enquiry details.
     */
    public void editDetails(String enquiryDetails) {
        if (isReplied()) {
            System.err.printf("Cannot modify replied enquiry");
            return;
        }
        if (enquiryDetails == null) {
            System.err.printf("Content cannot be empty");
            return;
        }

        this.enquiryDetails = enquiryDetails;
    }

    /**
     * Gets the ID of the enquiry.
     *
     * @return The enquiry ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the enquiry.
     *
     * @param id The new enquiry ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the NRIC of the applicant who submitted the enquiry.
     *
     * @return The applicant's NRIC.
     */
    public String getApplicantNric() {
        return applicantNric;
    }

    /**
     * Gets the name of the project the enquiry is about.
     *
     * @return The project name.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the details of the enquiry.
     *
     * @return The enquiry details.
     */
    public String getDetails() {
        return enquiryDetails;
    }

    /**
     * Sets the details of the enquiry.
     *
     * @param details The new enquiry details.
     */
    public void setDetails(String details) {
        this.enquiryDetails = details;
    }

    /**
     * Gets the time the enquiry was created.
     *
     * @return The creation time of the enquiry.
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * Gets the reply to the enquiry.
     *
     * @return The reply content, or {@code null} if no reply exists.
     */
    public String getReply() {
        return reply;
    }

    /**
     * Gets the name of the officer who replied to the enquiry.
     *
     * @return The name of the officer who replied, or {@code null} if no reply exists.
     */
    public String getRepliedBy() {
        return repliedBy;
    }

    /**
     * Gets the time the enquiry was replied to.
     *
     * @return The reply time, or {@code null} if no reply exists.
     */
    public LocalDateTime getRepliedTime() {
        return repliedTime;
    }

    /**
     * Checks if the enquiry has been replied to.
     *
     * @return {@code true} if the enquiry has a reply, {@code false} otherwise.
     */
    public boolean isReplied() {
        return reply != null && !reply.equals("null") && !reply.isEmpty();
    }
}