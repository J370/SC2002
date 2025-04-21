package bto.Model;

import java.time.LocalDateTime;

public class Enquiry {
    private int id;
    private final String applicantNric;
    private final String projectName;
    private String enquiryDetails;
    private final LocalDateTime createdTime;
    private String reply;
    private String repliedBy;
    private LocalDateTime repliedTime;

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

    public void setReply(String reply, String officerName) {
        if (reply == null ) { System.err.printf("Reply cannot be empty"); return; }
        if (this.reply != null) { System.err.printf("Enquiry already has a reply"); return; }
        
        this.reply = reply;
        this.repliedBy = officerName;
        this.repliedTime = LocalDateTime.now();
    }

    public void editDetails(String enquiryDetails) {
        if (isReplied()) { System.err.printf("Cannot modify replied enquiry"); return; }
        if (enquiryDetails == null) { System.err.printf("Content cannot be empty"); return; }
        
        this.enquiryDetails = enquiryDetails;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getApplicantNric() { return applicantNric; }
    public String getProjectName() { return projectName; }
    public String getDetails() { return enquiryDetails; }
    public void setDetails(String details) { this.enquiryDetails = details; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public String getReply() { return reply; }

    public String getRepliedBy() { return repliedBy; }
    public LocalDateTime getRepliedTime() { return repliedTime; }
    public boolean isReplied() { return reply != null && !reply.equals("null") && !reply.isEmpty(); }

}