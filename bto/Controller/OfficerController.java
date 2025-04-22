package bto.Controller;
import bto.Data.*;
import bto.Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class OfficerController {
    private final Officer officer;
    private final ApplicationDao applicationDao;
    private final ProjectDao projectDao;
    private final EnquiryDao enquiryDao;

    public OfficerController(Officer officer, 
                            ApplicationDao applicationDao,
                            ProjectDao projectDao,
                            EnquiryDao enquiryDao) {
        this.officer = officer;
        this.applicationDao = applicationDao;
        this.projectDao = projectDao;
        this.enquiryDao = enquiryDao;
    }

    // To register to be a registered officer for a specific project
    public void registerProject(String projectName) throws Exception {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new Exception("Project name cannot be empty");
        }

        // 1. Check if officer has applied for this project as an applicant
        if (hasAppliedToProject(projectName)) {
            throw new Exception("Cannot register for a project you've applied to as an applicant.");
        }
        
        Project targetProject;
        try {
            targetProject = projectDao.getProjectById(projectName);
            if (targetProject == null) {
                throw new Exception("Project not found: " + projectName);
            }
        } catch (NoSuchElementException e) {
            throw new Exception("Project not found: " + projectName);
        }
        
        
        // Date validations
        if (targetProject.getOpeningDate() == null || targetProject.getClosingDate() == null) {
            throw new Exception("Project has invalid dates");
        }
        
        for (Project p : projectDao.getAllProjects()) {
            boolean isRegistered = p.getRequestedOfficers().contains(officer.getName()) || p.getAssignedOfficers().contains(officer.getName());
            boolean overlap = !(targetProject.getClosingDate().isBefore(p.getOpeningDate()) || targetProject.getOpeningDate().isAfter(p.getClosingDate()));
                                
            if (isRegistered && overlap) {
                throw new Exception("Already registered or assigned to another project during this period.");
            }
        }

        // Add to requested officers if not already requested
        if (!targetProject.getRequestedOfficers().contains(officer.getName())) {
            targetProject.addRequestedOfficer(officer.getName());
            projectDao.updateProject(targetProject);
        } else throw new Exception("Already requested registration for this project.");
    }


    // register status to be approved by manager
    public List<RegistrationStatus> viewRegistrationStatus() {
        List<Project>  projects = projectDao.getAllProjects();
        List<RegistrationStatus> output = new ArrayList<>();
        for (Project p : projects){
            if (p.getAssignedOfficers().contains(officer.getName())) {
                output.add(new RegistrationStatus(p, "Assigned"));
            } else if (p.getRequestedOfficers().contains(officer.getName())) {
                output.add(new RegistrationStatus(p, "Pending"));
            } else if (p.getRejectedOfficers().contains(officer.getName())) {
                output.add(new RegistrationStatus(p, "Rejected"));
            }
        }
        return output;
    }


    //generate receipt for applicant once approved
    public String generateReceipt(Application application) throws Exception {
        if (application.getStatus() != ApplicationStatus.BOOKED) throw new Exception("Application is not booked");
            // Get applicant details
        User applicant = User.getUser(application.getApplicantNric());
        if (applicant == null) throw new Exception("Applicant not found.");

        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) throw new Exception("Project not found.");

        Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(application.getFlatType());
        if (flatDetails == null) throw new Exception("Flat type not found in project.");
        String receipt =
            "\n===== BTO Booking Receipt =====" +
            "\nApplicant Name: " + applicant.getName() +
            "\nNRIC: " + applicant.getNric() +
            "\nAge: " + applicant.getAge() +
            "\nMarital Status: " + applicant.getMaritalStatus() +
            "\nFlat Type Booked: " + application.getFlatType() +
            "\nProject Name: " + project.getName() +
            "\nNeighborhood: " + project.getNeighborhood() +
            "\nFlat Price: $" + flatDetails.getSellingPrice() +
            "\nBooking Status: " + application.getStatus() +
            "\n==============================";
    return receipt;
    }

    // view status of BTO applicants
    public List<Application> viewApplicationsForMyProjects() throws Exception {
        List<Application> result = new ArrayList<>();
        List<Project> allProjects = projectDao.getAllProjects();
        List<String> myProjectNames = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.getAssignedOfficers().contains(officer.getName())) {
                myProjectNames.add(p.getName());
            }
        }
        if (myProjectNames.isEmpty()) throw new Exception("You are not assigned to any projects");
        for (Application app : applicationDao.getAllApplications()) {
            if (myProjectNames.contains(app.getProjectName())) {
                result.add(app);
            }
        }
        return result;
    }

    // update status of BTO applicants
    public void updateStatus(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));
        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) throw new Exception("Project not found for this application");
        if (!project.getAssignedOfficers().contains(officer.getName())) throw new Exception("You are not assigned to this project.");
        if (application.getStatus() != ApplicationStatus.SUCCESS) throw new Exception("Only successful applications can be updated to booked.");
        
        String flatType = application.getFlatType();
        if (flatType != null && !flatType.isEmpty()) {
            Project.FlatTypeDetails details = project.getFlatTypes().get(flatType);
            if (details == null) {
                throw new Exception("Flat type not found in project.");
            }
            if (details.getAvailableUnits() < 1) {
                throw new Exception("No available units for flat type: " + flatType);
            }
            projectDao.decreaseAvailableUnits(application.getProjectName(), flatType, 1);
        }
    }

    
    // view all enquiries for officer assigned project
    public List<Enquiry> viewEnquiriesForMyProjects() throws Exception {
        List<Project> allProjects = projectDao.getAllProjects();
        if (allProjects == null) throw new Exception("No projects found");
        List<String> assignedProjectNames = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.getAssignedOfficers().contains(officer.getName())) {
                assignedProjectNames.add(p.getName());
            }
        }
        if (assignedProjectNames.isEmpty()) {
            throw new Exception("Officer is not assigned to any project");
        }
        List<Enquiry> result = new ArrayList<>();
        for (String projectName : assignedProjectNames) {
            result.addAll(enquiryDao.getEnquiriesByProject(projectName));
        }
        return result;
    }

    // Officer can reply to an enquiry for their project
    public void replyEnquiry(int enquiryId, String reply) throws Exception{
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (reply == null) throw new Exception("Reply cannot be empty");
        if (enquiry == null) throw new Exception("Enquiry not found");
        Project project = projectDao.getProjectById(enquiry.getProjectName());
        if (project == null || !project.getAssignedOfficers().contains(officer.getName())) {
            throw new Exception("You are not assigned to this project");
        }
        if (enquiry.isReplied()) throw new Exception("Enquiry already replied");
        enquiry.setReply(reply, officer.getName());
        enquiryDao.update(enquiry);
    }

    private boolean hasAppliedToProject(String projectName) {
        List<Application> applications = applicationDao.getAllApplications();
        for (Application app : applications) {
            if (officer.getNric().equals(app.getApplicantNric()) && projectName.equals(app.getProjectName())) return true;
        }
        return false;
    }

    public static class RegistrationStatus {
        public final Project project;
        public final String status; // "Pending", "Successful", "Unsuccessful"
        public RegistrationStatus(Project project, String status) {
            this.project = project;
            this.status = status;
        }
    }

}
