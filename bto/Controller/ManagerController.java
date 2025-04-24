package bto.Controller;

import bto.Data.*;
import bto.Model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing operations related to managers, such as creating, editing, and deleting projects,
 * handling officer registrations, managing applications, and responding to enquiries.
 */
public class ManagerController {
    private final Manager manager;
    private final ProjectDao projectDao;
    private final ApplicationDao applicationDao;
    private final EnquiryDao enquiryDao;

    /**
     * Constructs a ManagerController with the specified dependencies.
     *
     * @param manager The manager associated with this controller.
     * @param projectDao The DAO for managing projects.
     * @param applicationDao The DAO for managing applications.
     * @param enquiryDao The DAO for managing enquiries.
     */
    public ManagerController(Manager manager, ProjectDao projectDao, 
                            ApplicationDao applicationDao, EnquiryDao enquiryDao) {
        this.manager = manager;
        this.projectDao = projectDao;
        this.applicationDao = applicationDao;
        this.enquiryDao = enquiryDao;
    }

    /**
     * Creates a new project.
     *
     * @param name The name of the project.
     * @param neighborhood The neighborhood of the project.
     * @param flatTypes A map of flat types and their details.
     * @param openingDate The opening date for applications.
     * @param closingDate The closing date for applications.
     * @param officerSlots The number of officer slots available for the project.
     * @throws Exception If there is an active project or other validation issues.
     */
    public void createProject(String name, String neighborhood, Map<String, Project.FlatTypeDetails> flatTypes,
                             LocalDate openingDate, LocalDate closingDate, int officerSlots) throws Exception {
        if (hasActiveProject()) throw new Exception("Cannot create new project while managing active project");

        Project newProject = new Project(
            name,
            neighborhood,
            flatTypes,
            openingDate,
            closingDate,
            manager.getName(),
            officerSlots,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            true
        );
        projectDao.saveProject(newProject);
    }

    /**
     * Deletes a project by its name.
     *
     * @param projectName The name of the project to delete.
     * @throws Exception If the project is not found or the manager is not authorized to delete it.
     */
    public void deleteProject(String projectName) throws Exception {
        Project project = projectDao.getProjectById(projectName);
        if (project == null) throw new Exception("Project not found");
        if (!project.getManager().equals(manager.getName())) throw new Exception("Only project creator can delete project");

        projectDao.deleteProject(projectName);
    }

    /**
     * Edits an existing project.
     *
     * @param name The name of the project.
     * @param neighborhood The neighborhood of the project.
     * @param flatTypes A map of flat types and their details.
     * @param openingDate The opening date for applications.
     * @param closingDate The closing date for applications.
     * @param officerSlots The number of officer slots available for the project.
     * @throws Exception If the project cannot be updated.
     */
    public void editProject(String name, String neighborhood, Map<String, Project.FlatTypeDetails> flatTypes,
                             LocalDate openingDate, LocalDate closingDate, int officerSlots) throws Exception {
        Project newProject = new Project(
            name,
            neighborhood,
            flatTypes,
            openingDate,
            closingDate,
            manager.getName(),
            officerSlots,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            true
        );
        projectDao.updateProject(newProject);
    }

    /**
     * Retrieves all projects.
     *
     * @return A list of all projects.
     */
    public List<Project> viewAllProject() {
        return projectDao.getAllProjects();
    }

    /**
     * Retrieves all projects managed by the current manager.
     *
     * @return A list of projects managed by the manager.
     */
    public List<Project> viewOwnProjects() {
        List<Project> allProjects = projectDao.getAllProjects();
        List<Project> ownProjects = new ArrayList<>();
        for (Project p : allProjects) {
            if (p.getManager().toString().equals(manager.getName())) {
                ownProjects.add(p);
            }
        }
        return ownProjects;
    }

    /**
     * Retrieves a list of officer registration requests, assignments, and rejections for all projects.
     *
     * @return A list of officer registration details.
     */
    public List<String> viewRequestedOfficer() {
        List<String> requests = new ArrayList<>();
        List<Project> allProjects = projectDao.getAllProjects();
    
        for (Project project : allProjects) {
            List<String> requestedOfficers = project.getRequestedOfficers();
            if (!requestedOfficers.isEmpty()) {
                for (String officer : requestedOfficers) {
                    requests.add("Officer: " + officer + " applied for Project: " + project.getName());
                }
            }

            List<String> assignedOfficers = project.getAssignedOfficers();

            if (!assignedOfficers.isEmpty()) {
                for (String officer : assignedOfficers) {
                    requests.add("Officer: " + officer + " is assigned to Project: " + project.getName());
                }
            }

            List<String> rejectedOfficers = project.getRejectedOfficers();
            if (!rejectedOfficers.isEmpty()) {
                for (String officer : rejectedOfficers) {
                    requests.add("Officer: " + officer + " is rejected from Project: " + project.getName());
                }
            }
        }
        return requests;
    }

    /**
     * Approves an officer's registration for a project.
     *
     * @param projectName The name of the project.
     * @param officerName The name of the officer to approve.
     * @throws Exception If the officer is already assigned, not requested, or there are no available officer slots for the project.
     */
    public void approveRegistration(String projectName, String officerName) throws Exception{
        Project project = projectDao.getProjectById(projectName);

        if (project.getAssignedOfficers().contains(officerName)) throw new Exception("Officer " + officerName + " is already assigned to this project.");
        if (!project.getRequestedOfficers().contains(officerName)) {
            if (!project.getRejectedOfficers().contains(officerName)) {
                throw new Exception("Officer " + officerName + " is already assigned to this project.");
            }
            else {
                project.removeRejectedOfficer(officerName);
            }
        }
        else {
            if (project.getOfficerSlots() <= 0) throw new Exception("No available officer slots for project: " + projectName);
        }

        project.addAssignedOfficer(officerName);
        project.removeRequestedOfficer(officerName);
        project.setOfficerSlots(project.getOfficerSlots() - 1);
        projectDao.updateProject(project);
    }

    /**
     * Rejects an officer's registration for a project.
     *
     * @param projectName The name of the project.
     * @param officerName The name of the officer to reject.
     * @throws Exception If the officer is already rejected, not requested, or assigned to the project.
     */
    public void rejectRegistration(String projectName, String officerName) throws Exception {
        Project project = projectDao.getProjectById(projectName);

        if (project.getRejectedOfficers().contains(officerName)) throw new Exception("Officer " + officerName + " has already been rejected for this project.");
        if (!project.getRequestedOfficers().contains(officerName)) {
            if (!project.getAssignedOfficers().contains(officerName)) {
                throw new Exception("Officer " + officerName + " is already assigned to this project.");
            }
            else {
                project.removeAssignedOfficer(officerName);
            }
        }
        else {
            if (project.getOfficerSlots() <= 0) throw new Exception("No available officer slots for project: " + projectName);
        }
        

        project.addRejectedOfficer(officerName);
        project.removeRequestedOfficer(officerName);
        project.setOfficerSlots(project.getOfficerSlots() + 1);
        projectDao.updateProject(project);
    }

    /**
     * Approves an application.
     *
     * @param applicationId The ID of the application to approve.
     * @throws Exception If the application is not found, is not pending, or there are no available units for the flat type.
     */
    public void approveApplication(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));

        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) {throw new Exception("Associated project not found");}

        if (application.getStatus() != ApplicationStatus.PENDING) throw new Exception("Only pending applications can be approved");

        String flatType = application.getFlatType();
        Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(flatType);
        
        if (flatDetails == null || flatDetails.getAvailableUnits() <= 0) throw new Exception("No available units for " + flatType + " flats");

        application.setStatus(ApplicationStatus.SUCCESS);
        applicationDao.update(application);

    }

    /**
     * Rejects an application.
     *
     * @param applicationId The ID of the application to reject.
     * @throws Exception If the application is not found, is not pending, or there are no available units for the flat type.
     */
    public void rejectApplication(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));

        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) {throw new Exception("Associated project not found");}

        if (application.getStatus() != ApplicationStatus.PENDING) throw new Exception("Only pending applications can be rejected");

        String flatType = application.getFlatType();
        Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(flatType);
        
        if (flatDetails == null || flatDetails.getAvailableUnits() <= 0) throw new Exception("No available units for " + flatType + " flats");

        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
        applicationDao.update(application);
    }

    /**
     * Approves a withdrawal request for an application.
     *
     * @param applicationId The ID of the application to approve withdrawal for.
     * @throws Exception If the application is not found, no withdrawal is requested, or the associated project is not found.
     */
    public void approveWithdrawal(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));
    
        if (!application.getWithdrawalRequested()) throw new Exception("No withdrawal requested for this application.");
    
        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) throw new Exception("Associated project not found");

        if (application.getStatus() == ApplicationStatus.BOOKED) {
            String flatType = application.getFlatType();
            Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(flatType);
            if (flatDetails != null) {
                flatDetails.setAvailableUnits(flatDetails.getAvailableUnits() + 1);
                projectDao.updateProject(project);
            }
        }

        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
        application.setWithdrawalRequested(false);
        applicationDao.update(application);
    }
    
    /**
     * Rejects a withdrawal request for an application.
     *
     * @param applicationId The ID of the application to reject withdrawal for.
     * @throws Exception If the application is not found or no withdrawal is requested.
     */
    public void rejectWithdrawal(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));
    
        if (!application.getWithdrawalRequested()) throw new Exception("No withdrawal requested for this application.");
    
        application.setWithdrawalRequested(false);
        applicationDao.update(application);
    }

    /**
     * Retrieves all enquiries from the system.
     *
     * @return A list of all enquiries.
     */
    public List<Enquiry> viewAllEnquiries() {
        return enquiryDao.getAllEnquiries();
    }

    /**
     * Replies to a specific enquiry.
     *
     * @param enquiryId The ID of the enquiry to reply to.
     * @param reply The reply message to be sent.
     * @throws Exception If the reply is empty, the enquiry is not found, or the manager is not authorized to reply.
     */
    public void replyEnquiry(int enquiryId, String reply) throws Exception {
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (reply == null) throw new Exception("Reply cannot be empty");
        if (enquiry == null) throw new Exception("Enquiry not found");
        Project project = projectDao.getProjectById(enquiry.getProjectName());
        if (project == null || !project.getManager().equals(manager.getName())) {
            throw new Exception("You are not authorized to reply to this enquiry.");
        }
        if (enquiry.isReplied()) throw new Exception("Enquiry already replied");
        enquiry.setReply(reply, manager.getName());
        enquiryDao.update(enquiry);
    }

    /**
     * Generates a report of all booked applications.
     *
     * @return A list of applications with the status {@code BOOKED}.
     */
    public List<Application> generateReport() {
        List<Application> allApplications = applicationDao.getAllApplications();
        List<Application> bookedApplications = new ArrayList<>();
        if (allApplications != null) {
            for (Application app : allApplications) {
                if (app.getStatus() == ApplicationStatus.BOOKED) {
                    bookedApplications.add(app);
                }
            }
        }
        return bookedApplications;
    }

    /**
     * Toggles the visibility of a project.
     *
     * @param projectName The name of the project whose visibility is to be toggled.
     * @throws Exception If the project is not found.
     */
    public void toggleProjectVisibility(String projectName) throws Exception {
        Project project = projectDao.getProjectById(projectName);
        if (project == null) throw new Exception("Project not found");
        project.toggleVisibility();
        projectDao.updateProject(project);
    }

    /**
     * Checks if the manager has an active project.
     *
     * @return {@code true} if the manager has an active project, {@code false} otherwise.
     */
    private boolean hasActiveProject() {
        return projectDao.getAllProjects().stream()
            .filter(p -> p.getManager().equals(manager.getName()))
            .anyMatch(p -> p.isVisible() && p.isApplicationOpen());
    }

    /**
     * Retrieves all applications.
     *
     * @return A list of all applications.
     */
    public List<Application> viewAllApplications() {
        return applicationDao.getAllApplications();
    }
}
