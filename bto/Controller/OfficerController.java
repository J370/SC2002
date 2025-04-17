package bto.Controller;
import java.util.ArrayList;
import java.util.List;
import bto.Data.*;
import bto.Model.*;

public class OfficerController {
    private final Officer officer;
    private final ApplicationDao applicationDao;
    private final ProjectDao projectDao;
    private final EnquiryDao enquiryDao;

    public OfficerController(Officer officer, 
                            ApplicationDao applicationDao,
                            ProjectDao projectDao,
                            EnquiryDao enquiryDao) {
        this.Officer = officer;
        this.applicationDao = applicationDao;
        this.projectDao = projectDao;
        this.enquiryDao = enquiryDao;
    }

    // To register to be a registered officer for a specific project
    public void registerProject(Project project) {
        if (projectDao.getProjectByName(project.getName()) != null) throw new IllegalStateException("Project already exists");
    }

    // register status to be approved by manager
    public void viewRegistrationStatus(){

    }


    //generate receipt for applicant once approved
    public void generateReceipt(Application application) {
        if (application.getStatus() != ApplicationStatus.BOOKED) throw new IllegalStateException("Application is not booked");
        String receipt = "Receipt for Application ID: " + application.getId() + "\n" +
                         "Applicant NRIC: " + application.getApplicantNric() + "\n" +
                         "Project Name: " + application.getProject().getName() + "\n" +
                         "Flat Type: " + application.getFlatType() + "\n" +
                         "Status: " + application.getStatus() + "\n" +
                         "Booking Fee: $" + application.getBookingFee() + "\n";
        System.out.println(receipt);
    }

    // update status of BTO applicants
    public void updateStatus(Application application, ApplicationStatus status) {
        if (application.getStatus() == ApplicationStatus.BOOKED) throw new IllegalStateException("Cannot update booked application status");
        application.setStatus(status);
        applicationDao.updateApplication(application);
    }

    // view all enquiries for officer assigned project
    public void viewEnquiry() {
        List<Enquiry> enquiries = enquiryDao.getAllEnquiries();
        if (enquiries.isEmpty()) throw new IllegalStateException("No enquiries found");
        EnquiryView.displayEnquiries(enquiries);
    }

    // reply to an enquiry that is under officer control
    public void replyEnquiry(Enquiry enquiry, String reply) {
        if (enquiry.getStatus() != EnquiryStatus.PENDING) throw new IllegalStateException("Enquiry is not pending");
        enquiry.setStatus(EnquiryStatus.REPLIED);
        enquiry.setReply(reply);
        enquiryDao.updateEnquiry(enquiry);
    }

}
