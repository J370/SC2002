package bto.Controller;
import bto.Data.*;
import bto.Model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerController {
    private final Manager manager;
    private final ProjectDao projectDao;
    private final ApplicationDao applicationDao;
    private final EnquiryDao enquiryDao;
    
    public ManagerController(Manager manager, ProjectDao projectDao, 
                            ApplicationDao applicationDao, EnquiryDao enquiryDao) {
        this.manager = manager;
        this.projectDao = projectDao;
        this.applicationDao = applicationDao;
        this.enquiryDao = enquiryDao;
    }

    public void createProject(String name, String neighborhood, Map<String, Project.FlatTypeDetails> flatTypes,
                             LocalDate openingDate, LocalDate closingDate,int officerSlots) throws Exception {
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


    public void deleteProject(String projectName) throws Exception {
        Project project = projectDao.getProjectById(projectName);
        if (project == null) throw new Exception("Project not found");
        if (!project.getManager().equals(manager.getName())) throw new Exception("Only project creator can delete project");

        projectDao.deleteProject(projectName);
    }

    public void editProject(String name, String neighborhood, Map<String, Project.FlatTypeDetails> flatTypes,
                             LocalDate openingDate, LocalDate closingDate,int officerSlots) throws Exception {

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

    public List<Project> viewAllProject(){
        return projectDao.getAllProjects();
    }

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

    public void rejectRegistration(String projectName, String officerName) throws Exception{
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

    public void approveWithdrawal(String applicationId) throws Exception {
        Application application = applicationDao.getApplicationById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));


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
        applicationDao.update(application);
    }

    public void rejectWithdrawal(String applicationId) throws Exception {
        
    }

    public List<Enquiry> viewAllEnquiries(){return enquiryDao.getAllEnquiries();}

    public void replyEnquiry(int enquiryId, String reply) throws Exception{
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


    public void toggleProjectVisibility(String projectName) throws Exception {
        Project project = projectDao.getProjectById(projectName);
        if (project == null) throw new Exception("Project not found");
        project.toggleVisibility();
        projectDao.updateProject(project);
    }


    private boolean hasActiveProject() {
        return projectDao.getAllProjects().stream()
            .filter(p -> p.getManager().equals(manager.getName()))
            .anyMatch(p -> p.isVisible() && p.isApplicationOpen());
    }

    public List<Application> viewAllApplications() {
        return applicationDao.getAllApplications();
    }
}
