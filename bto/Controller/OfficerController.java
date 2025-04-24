package bto.Controller;

import bto.Data.*;
import bto.Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller class for managing officer-related operations, such as project registration,
 * application management, and enquiry handling.
 */
public class OfficerController {
    private final Officer officer;
    private final ApplicationDao applicationDao;
    private final ProjectDao projectDao;
    private final EnquiryDao enquiryDao;

    /**
     * Constructs an OfficerController with the specified dependencies.
     *
     * @param officer The officer associated with this controller.
     * @param applicationDao The DAO for managing applications.
     * @param projectDao The DAO for managing projects.
     * @param enquiryDao The DAO for managing enquiries.
     */
    public OfficerController(Officer officer, 
                            ApplicationDao applicationDao,
                            ProjectDao projectDao,
                            EnquiryDao enquiryDao) {
        this.officer = officer;
        this.applicationDao = applicationDao;
        this.projectDao = projectDao;
        this.enquiryDao = enquiryDao;
    }

    /**
     * Registers the officer for a project.
     *
     * @param projectName The name of the project to register for.
     * @throws Exception If the registration fails due to validation issues.
     */
    public void registerProject(String projectName) throws Exception {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new Exception("Project name cannot be empty");
        }

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

        if (targetProject.getRejectedOfficers().contains(officer.getName())) {
            throw new Exception("You have been rejected for this project.");
        }

        if (!targetProject.getRequestedOfficers().contains(officer.getName())) {
            targetProject.addRequestedOfficer(officer.getName());
            projectDao.updateProject(targetProject);
        } else throw new Exception("Already requested registration for this project.");
    }

    /**
     * Views the registration status of the officer for all projects.
     *
     * @return A list of registration statuses for the officer.
     */
    public List<RegistrationStatus> viewRegistrationStatus() {
        List<Project> projects = projectDao.getAllProjects();
        List<RegistrationStatus> output = new ArrayList<>();
        for (Project p : projects) {
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

    /**
     * Generates a receipt for a booked application.
     *
     * @param applicationId The ID of the application.
     * @return A string containing the receipt details.
     * @throws Exception If the application is not found or is not booked.
     */
    public String generateReceipt(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));
        if (application.getStatus() != ApplicationStatus.BOOKED) throw new Exception("Application is not booked");
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

    /**
     * Retrieves all applications for projects assigned to the officer.
     *
     * @return A list of applications for the officer's projects.
     * @throws Exception If the officer is not assigned to any projects.
     */
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

    /**
     * Updates the status of an application to "booked".
     *
     * @param applicationId The ID of the application to update.
     * @throws Exception If the application cannot be updated.
     */
    public void updateStatus(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));
        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) throw new Exception("Project not found for this application");
        if (!project.getAssignedOfficers().contains(officer.getName())) throw new Exception("You are not assigned to this project.");
        if (application.getStatus() == ApplicationStatus.BOOKED) throw new Exception("Application is already booked.");
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

    /**
     * Retrieves all enquiries for projects assigned to the officer.
     *
     * @return A list of enquiries for the officer's projects.
     * @throws Exception If no projects are assigned to the officer.
     */
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

    /**
     * Replies to an enquiry for a project assigned to the officer.
     *
     * @param enquiryId The ID of the enquiry to reply to.
     * @param reply The reply message.
     * @throws Exception If the reply cannot be processed.
     */
    public void replyEnquiry(int enquiryId, String reply) throws Exception {
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

    /**
     * Checks if the officer has applied to a project as an applicant.
     *
     * @param projectName The name of the project.
     * @return {@code true} if the officer has applied, {@code false} otherwise.
     */
    private boolean hasAppliedToProject(String projectName) {
        List<Application> applications = applicationDao.getAllApplications();
        for (Application app : applications) {
            if (officer.getNric().equals(app.getApplicantNric()) && projectName.equals(app.getProjectName())) return true;
        }
        return false;
    }

    /**
     * Represents the registration status of an officer for a project.
     */
    public static class RegistrationStatus {
        /**
         * The project associated with the registration.
         */
        public final Project project;

        /**
         * The status of the registration (e.g., "Assigned", "Pending", "Rejected").
         */
        public final String status;

        /**
         * Constructs a RegistrationStatus object.
         *
         * @param project The project associated with the registration.
         * @param status The status of the registration (e.g., "Assigned", "Pending", "Rejected").
         */
        public RegistrationStatus(Project project, String status) {
            this.project = project;
            this.status = status;
        }
    }

    /**
     * Retrieves all projects.
     *
     * @return A list of all projects.
     */
    public List<Project> getAllProjects() {
        return projectDao.getAllProjects();
    }
}
