package bto.Controller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bto.Data.*;
import bto.Model.*;

public class ManagerController {
    private final Manager manager;
    private final ProjectCSVDao projectDao;
    private final ApplicationCSVDao applicationDao;
    private final EnquiryCSVDao enquiryDao;
    
    public ManagerController(Manager manager, ProjectCSVDao projectDao, 
                            ApplicationCSVDao applicationDao, EnquiryCSVDao enquiryDao) {
        this.manager = manager;
        this.projectDao = projectDao;
        this.applicationDao = applicationDao;
        this.enquiryDao = enquiryDao;
    }

    public void createProject(String name, String neighborhood, Map<String, Project.FlatTypeDetails> flatTypes,
                             LocalDate openingDate, LocalDate closingDate,int officerSlots) throws Exception {
        if (projectDao.getProjectById(name) != null) throw new Exception("Project name must be unique");
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
        if (!project.getManager().equals(manager.getNric())) throw new Exception("Only project creator can delete project");
        if (project.isApplicationOpen()) throw new Exception("Cannot delete project during active application period");

        projectDao.deleteProject(projectName);
    }

    public void manageProject(){

    }

    public List<Project> viewAllProject(){
        return projectDao.getAllProjects();
    }

    public List<Project> viewOwnProjects() {
        List<Project> allProjects = projectDao.getAllProjects();
        List<Project> ownProjects = new ArrayList<>();
        
        for (Project p : allProjects) {
            if (p.getManager().equals(manager.getNric())) {
                ownProjects.add(p);
            }
        }
        return ownProjects;
    }

    // see all requests to be officer of project (requested officer line in ProjectList.csv)
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
        }
        return requests;
    }

    //approve officer registration request to be officer of project
    public void approveRegistration(String projectName, String officerName) throws Exception{
        Project project = projectDao.getProjectById(projectName);
        
        if (project.getAssignedOfficers().size() >= project.getOfficerSlots()) {
            throw new Exception("No available officer slots");
        }

        project.addAssignedOfficer(officerName);
        project.removeRequestedOfficer(officerName);
        projectDao.updateProject(project);
    }

    public void rejectRegistration(String projectName, String officerName){
        Project project = projectDao.getProjectById(projectName);
        project.addRejectedOfficer(officerName);
        project.removeRequestedOfficer(officerName);
        projectDao.updateProject(project);
    }

    // approve applicant bto request - approval is limited to supply of flats
    public void approveApplication(String applicationId) throws Exception {
        Application application = applicationDao.findById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));

        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) {throw new Exception("Associated project not found");}

        // Check application status
        if (application.getStatus() != ApplicationStatus.PENDING) throw new Exception("Only pending applications can be approved");

        // Check flat availability
        String flatType = application.getFlatType();
        Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(flatType);
        
        if (flatDetails == null || flatDetails.getAvailableUnits() <= 0) throw new Exception("No available units for " + flatType + " flats");

        application.setStatus(ApplicationStatus.SUCCESS);
        applicationDao.update(application);

        // Update project inventory
        flatDetails.setAvailableUnits(flatDetails.getAvailableUnits() - 1);
        projectDao.updateProject(project);
    }

    // Approve withdrawal request
    public void approveWithdrawal(String applicationId) throws Exception {
        Application application = applicationDao.findById(applicationId)
            .orElseThrow(() -> new Exception("Application not found"));

        // Check booking status
        if (application.getStatus() == ApplicationStatus.BOOKED) {throw new Exception("Cannot withdraw after flat booking");}

        Project project = projectDao.getProjectById(application.getProjectName());
        if (project == null) throw new Exception("Associated project not found");

        // Restock flat if application was successful
        if (application.getStatus() == ApplicationStatus.SUCCESS) {
            String flatType = application.getFlatType();
            Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(flatType);
            
            if (flatDetails != null) {
                flatDetails.setAvailableUnits(flatDetails.getAvailableUnits() + 1);
                projectDao.updateProject(project);
            }
        }

        // Update application status
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
        applicationDao.update(application);
    }


    // view all enquiries of ALL projects
    public List<Enquiry> viewAllEnquiries(){return enquiryDao.getAllEnquiries();}

    //able to view and reply to enquiries regarding the project manager is handling
    public void replyEnquiry(int enquiryId, String reply) throws Exception{
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (enquiry == null) throw new Exception("Enquiry not found");
        if (enquiry.isReplied()) throw new Exception("Enquiry already replied");
        enquiry.setReply(reply, manager.getName());
        enquiryDao.update(enquiry);
    }

    // generate report of applicants with respective flat booking – flat type, project name, age, marital status
    public void generateReport(){

    }

    // toggle project visibility

    public void toggleProjectVisibility(String projectName) {
        Project project = projectDao.getProjectById(projectName);
        project.toggleVisibility();
        projectDao.updateProject(project);
    }


    private boolean hasActiveProject() {
        return projectDao.getAllProjects().stream()
            .filter(p -> p.getManager().equals(manager.getNric()))
            .anyMatch(p -> p.isVisible() && p.isApplicationOpen());
    }
}
