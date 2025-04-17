package bto.Controller;
import bto.Data.*;
import bto.Model.*;
import bto.View.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public class ApplicantController {
    private final Applicant applicant;
    private final ApplicationDao applicationDao;
    private final ProjectDao projectDao;
    private final EnquiryDao enquiryDao;

    public ApplicantController(Applicant applicant, 
                              ApplicationDao applicationDao,
                              ProjectDao projectDao,
                              EnquiryDao enquiryDao) {
        this.applicant = applicant;
        this.applicationDao = applicationDao;
        this.projectDao = projectDao;
        this.enquiryDao = enquiryDao;
    }

    public void viewAvailableProjects(){
        List<Project> visibleProjects = new ArrayList<>();
        List<Project> allProjects = projectDao.getAllProjects();
        ApplicantView applicantView = new ApplicantView(applicant);
    
        for (Project project : allProjects) {
            if (project.isVisible() && project.isEligible(applicant)) visibleProjects.add(project);
        }
        applicantView.displayProjects(visibleProjects, applicant.getMaritalStatus());
    }
    

    public void applyProject(String projectName) throws Exception {
        Project project = projectDao.getProjectById(projectName);
        if (applicationDao.getActiveApplication(applicant.getNric()).isPresent()) throw new Exception("You already have an active application!"); 
        
        String flatType = "2-Room"; // Default for singles
        if (applicant.getMaritalStatus().equals("Married")) {
            List<String> availableTypes = project.getAvailableFlatTypes();
            if (availableTypes.isEmpty()) throw new Exception("No available flat types in this project");
            //flatType = availableTypes.size() > 1 ? applicantView.promptFlatType() : availableTypes.get(0);
        }

        if (!project.hasAvailableUnits(flatType)) throw new Exception("No available units for selected flat type");

        Application application = new Application(project.getName(), applicant.getNric(), flatType);
        applicationDao.save(application);
    }


    public Application viewApplication() throws Exception {
        Optional<Application> optionalApp = applicationDao.getActiveApplication(applicant.getNric());

        if (optionalApp.isEmpty()) throw new Exception("No active application found for applicant NRIC: " + applicant.getNric());

        return optionalApp.get();
    }

    public void withdrawApplication() throws Exception {
        Application app = viewApplication();
        if (app.getStatus() == ApplicationStatus.BOOKED) throw new Exception("Cannot withdraw application that has been booked!");
        app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        applicationDao.update(app);
    }

    public void submitEnquiry(String projectName, String details) {
        Project project = projectDao.getProjectById(projectName);
        Enquiry enquiry = new Enquiry(0,
            applicant.getNric(),
            project.getName(),    
            details,              
            LocalDateTime.now(),
            null,
            null,
            null
        );
        enquiryDao.save(enquiry);
    }

    public List<Enquiry> viewEnquiries() {
        List<Enquiry> allEnquiries = enquiryDao.getAllEnquiries();
        List<Enquiry> userEnquiries = new ArrayList<>();
        
        for (Enquiry enquiry : allEnquiries) {
            if (enquiry.getApplicantNric().equals(applicant.getNric())) {
                userEnquiries.add(enquiry);
            }
        }
        return userEnquiries;
    }

    public void editEnquiry(int enquiryId, String newDetails) throws Exception {
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (!enquiry.getApplicantNric().equals(applicant.getNric())) throw new Exception("You can only edit your own enquiries");
        if(enquiry.getReply() != null) throw new Exception("Cannot edit enquiries that has been replied");
        enquiry.editDetails(newDetails);
        enquiryDao.update(enquiry);
    }

    public void deleteEnquiry(int enquiryId) throws Exception {
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (!enquiry.getApplicantNric().equals(applicant.getNric())) throw new Exception("You can only delete your own enquiries");
        if(enquiry.getReply() != null) throw new Exception("Cannot delete enquiries that has been replied");
        enquiryDao.delete(enquiry.getId());
    }
}
