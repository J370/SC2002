package bto.Controller;
import bto.Data.*;
import bto.Model.*;
import java.util.ArrayList;
import java.util.List;

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
    
        for (Project project : allProjects) {
            if (project.isVisible() && project.isEligible(applicant)) visibleProjects.add(project);
        }
        //ProjectView.displayProjects(visibleProjects, applicant.getMaritalStatus());

    }
    

    public void applyProject(Project project) {
        if (applicationDao.getActiveApplication(applicant.getNric()).isPresent()) throw new IllegalStateException("You already have an active application");
        
        String flatType = "2-Room"; // Default for singles
        if (applicant.getMaritalStatus().equals("Married")) {
            List<String> availableTypes = project.getAvailableFlatTypes();
            if (availableTypes.isEmpty()) throw new IllegalStateException("No available flat types in this project");
            //flatType = availableTypes.size() > 1 ? ProjectView.promptFlatType() : availableTypes.get(0);
        }

        if (!project.hasAvailableUnits(flatType)) throw new IllegalStateException("No available units for selected flat type");

        Application application = new Application(project.getName(), applicant.getNric(), flatType);
        applicationDao.save(application);
    }


    public Application viewApplication() {
        return applicationDao.getActiveApplication(applicant.getNric())
            .orElseThrow(() -> new IllegalStateException("No active application found"));
    }

    public void withdrawApplication() {
        Application app = viewApplication();
        if (app.getStatus() == ApplicationStatus.BOOKED) throw new IllegalStateException("Cannot withdraw after booking");
        app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        applicationDao.update(app);
    }

    public void submitEnquiry(String details, Project project) {
        Enquiry enquiry = new Enquiry(0,
            applicant.getNric(),  
            project.getName(),    
            details,              
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

    public void editEnquiry(Enquiry enquiry, String newDetails){
        if (!enquiry.getApplicantNric().equals(applicant.getNric())) throw new SecurityException("You can only edit your own enquiries");
        if(enquiry.getReply() == null) throw new SecurityException("Cannot edit enquiries that has been replied");
        enquiry.editDetails(newDetails);
        enquiryDao.update(enquiry);
    }

    public void deleteEnquiry(Enquiry enquiry) {
        if (!enquiry.getApplicantNric().equals(applicant.getNric())) throw new SecurityException("You can only delete your own enquiries");
        if(enquiry.getReply() != null) throw new SecurityException("Cannot delete enquiries that has been replied");
        enquiryDao.delete(enquiry.getId());
    }
}
