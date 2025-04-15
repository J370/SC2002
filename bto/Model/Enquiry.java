package bto.Model;


public class Enquiry {
    private final String applicantNric;
    private final String projectName;
    private String enquiryDetails;
    private String reply;    

        public Enquiry(String applicantNric, String projectName, String enquiryDetails, String reply) {
        
        this.applicantNric = applicantNric;
        this.projectName = projectName;
        this.enquiryDetails = enquiryDetails;
        this.reply = reply;
    }

    public void addReply(String reply, String officerId) {
        if (reply == null )throw new IllegalArgumentException("Reply cannot be empty");
        if (this.reply != null) throw new IllegalStateException("Enquiry already has a reply");
        
        this.reply = reply;
        //this.repliedBy = officerId;
        //this.repliedAt = LocalDateTime.now();
    }

    public void editDetails(String enquiryDetails) {
        if (isReplied()) throw new IllegalStateException("Cannot modify replied enquiry");
        if (enquiryDetails == null) throw new IllegalArgumentException("Content cannot be empty");
        
        this.enquiryDetails = enquiryDetails;
    }

    public boolean isReplied() {return reply != null && !reply.isEmpty();}
    public String getApplicantNric() { return applicantNric; }
    public String getProjectName() { return projectName; }
    public String getDetails() { return enquiryDetails; }
    public String getReply() { return reply; }

}