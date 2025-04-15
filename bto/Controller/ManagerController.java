package bto.Controller;
import java.util.ArrayList;
import java.util.List;
import bto.Data.*;
import bto.Model.*;

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

    public void createProject(){

    }

    public void deleteProject(){

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


    //approve officer registration request to be officer of project
    public void approveRegistration(String projectName, String officerId){
        Project project = projectDao.getProjectById(projectName);
        
        if (project.getAssignedOfficers().size() >= project.getOfficerSlots()) {
            throw new IllegalStateException("No available officer slots");
        }

        project.addOfficer(officerId);
        projectDao.updateProject(project);
    }


    // approve applicant bto request - approval is limited to supply of flats
    public void approveApplication(){

    }

    // approve applicant withdraw bto request
    public void approveWithdrawal(){

    }

    // view all enquiries of ALL projects
    public void viewAllEnquiries(){

    }

    //able to view and reply to enquiries regarding the project manager is handling
    public void replyEnquiry(){

    }

    // generate report of applicants with respective flat booking – flat type, project name, age, marital status
    public void generateReport(){

    }
}
