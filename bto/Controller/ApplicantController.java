package bto.Controller;

import bto.Data.*;
import bto.Model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Controller class for managing applicant-related operations, such as viewing projects,
 * submitting applications, and handling enquiries.
 */
public class ApplicantController {
    private final User applicant;
    private final ApplicationDao applicationDao;
    private final ProjectDao projectDao;
    private final EnquiryDao enquiryDao;

    /**
     * Constructs an ApplicantController with the specified dependencies.
     *
     * @param applicant The user associated with this controller.
     * @param applicationDao The DAO for managing applications.
     * @param projectDao The DAO for managing projects.
     * @param enquiryDao The DAO for managing enquiries.
     */
    public ApplicantController(User applicant, 
                              ApplicationDao applicationDao,
                              ProjectDao projectDao,
                              EnquiryDao enquiryDao) {
        this.applicant = applicant;
        this.applicationDao = applicationDao;
        this.projectDao = projectDao;
        this.enquiryDao = enquiryDao;
    }

    /**
     * Retrieves a list of projects available for the applicant.
     *
     * @return A list of visible projects.
     */
    public List<Project> getAvailableProjects() {
        List<Project> visibleProjects = new ArrayList<>();
        List<Project> allProjects = projectDao.getAllProjects();
        for (Project project : allProjects) {
            if (!project.isVisible()) continue;
            if (applicant instanceof Officer) {
                if (project.getAssignedOfficers().contains(applicant.getName())) continue;
            }
            visibleProjects.add(project);
        }
        return visibleProjects;
    }

    /**
     * Retrieves all applications submitted by the applicant.
     *
     * @return A list of applications associated with the applicant.
     */
    public List<Application> getAllApplications() {
        List<Application> allApplications = applicationDao.getAllApplications();
        List<Application> result = new ArrayList<>();
        for (Application app : allApplications) {
            if (app.getApplicantNric().equals(applicant.getNric())) {
                result.add(app);
            }
        }
        return result;
    }

    /**
     * Submits an application for a project with the specified flat type.
     *
     * @param projectName The name of the project to apply for.
     * @param flatType The type of flat to apply for.
     * @throws Exception If the application cannot be submitted.
     */
    public void applyProject(String projectName, String flatType) throws Exception {
        Project project = projectDao.getProjectById(projectName);
        
        if (project == null || !project.isVisible()) throw new NoSuchElementException("Project with name " + projectName + " not found");

        if (applicationDao.getActiveApplication(applicant.getNric()).isPresent())
            throw new Exception("You already have an active application!");
    
        if (applicant instanceof Officer) {
            if (project.getAssignedOfficers().contains(applicant.getName())) {
                throw new Exception("Officers cannot apply for projects they are assigned to.");
            }
        }

        Project.FlatTypeDetails details = project.getFlatTypes().get(flatType);
        if (details == null) throw new Exception("Flat type not found in this project.");
        if (details.getAvailableUnits() <= 0) throw new Exception("No available units for selected flat type.");
    
        Application application = new Application(project.getName(), applicant.getNric(), flatType);
        applicationDao.save(application);
    }

    /**
     * Retrieves the active application for the applicant.
     *
     * @return The active application.
     * @throws Exception If no active application is found.
     */
    public Application viewActiveApplication() throws Exception {
        Optional<Application> optionalApp = applicationDao.getActiveApplication(applicant.getNric());

        if (optionalApp.isEmpty()) throw new Exception("No active application found for applicant NRIC: " + applicant.getNric());

        return optionalApp.get();
    }

    /**
     * Requests withdrawal of the active application.
     *
     * @throws Exception If the application cannot be withdrawn.
     */
    public void withdrawApplication() throws Exception {
        Application app = viewActiveApplication();
        if (app.getWithdrawalRequested()) throw new Exception("Withdrawal already requested.");
        if (app.getStatus() == ApplicationStatus.UNSUCCESSFUL) throw new Exception("Cannot withdraw an unsuccessful application.");
        app.setWithdrawalRequested(true);
        applicationDao.update(app);
    }

    /**
     * Submits an enquiry for a project.
     *
     * @param projectName The name of the project to submit an enquiry for.
     * @param details The details of the enquiry.
     */
    public void submitEnquiry(String projectName, String details) {
        Project project = projectDao.getProjectById(projectName);
        if (project == null) throw new NoSuchElementException("Project with name " + projectName + " not found");

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

    /**
     * Retrieves all enquiries submitted by the applicant.
     *
     * @return A list of enquiries associated with the applicant.
     */
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

    /**
     * Edits the details of an existing enquiry.
     *
     * @param enquiryId The ID of the enquiry to edit.
     * @param newDetails The new details for the enquiry.
     * @throws Exception If the enquiry cannot be edited.
     */
    public void editEnquiry(int enquiryId, String newDetails) throws Exception {
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (!enquiry.getApplicantNric().equals(applicant.getNric())) throw new Exception("You can only edit your own enquiries");
        if (enquiry.getReply() != null) throw new Exception("Cannot edit enquiries that have been replied to");
        enquiry.editDetails(newDetails);
        enquiryDao.update(enquiry);
    }

    /**
     * Deletes an existing enquiry.
     *
     * @param enquiryId The ID of the enquiry to delete.
     * @throws Exception If the enquiry cannot be deleted.
     */
    public void deleteEnquiry(int enquiryId) throws Exception {
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (enquiry == null) throw new Exception("Enquiry not found");
        if (!enquiry.getApplicantNric().equals(applicant.getNric())) throw new Exception("You can only delete your own enquiries");
        if (enquiry.getReply() != null) throw new Exception("Cannot delete enquiries that have been replied to");
        enquiryDao.delete(enquiry.getId());
    }
}
