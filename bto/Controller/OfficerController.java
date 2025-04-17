package bto.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        this.officer = officer;
        this.applicationDao = applicationDao;
        this.projectDao = projectDao;
        this.enquiryDao = enquiryDao;
    }

    // To register to be a registered officer for a specific project
    public void registerProject(String projectName) throws Exception {
        // 1. Check if officer has applied for this project as an applicant
        if (hasAppliedToProject(projectName)) {
            throw new Exception("Cannot register for a project you've applied to as an applicant.");
        }
    
        Project targetProject = projectDao.getProjectById(projectName);
        for (Project p : projectDao.getAllProjects()) {
            boolean isRegistered = p.getRequestedOfficers().contains(officer.getName()) || p.getAssignedOfficers().contains(officer.getName());
            boolean overlap = !(targetProject.getClosingDate().isBefore(p.getOpeningDate()) || targetProject.getOpeningDate().isAfter(p.getClosingDate()));
            if (isRegistered && overlap) {
                throw new Exception("Already registered or assigned to another project during this period.");
            }
        }
    
        // 3. Add to requested officers if not already requested
        if (!targetProject.getRequestedOfficers().contains(officer.getName())) {
            targetProject.addRequestedOfficer(officer.getName());
            projectDao.updateProject(targetProject);
        } else throw new Exception("Already requested registration for this project.");
    }

    // register status to be approved by manager
    public List<Project> viewRegistrationStatus() {
        return projectDao.getAllProjects().stream()
            .filter(p -> p.getRequestedOfficers().contains(officer.getNric()))
            .collect(Collectors.toList());
    }


    //generate receipt for applicant once approved
    public String generateReceipt(Application application) throws Exception {
        if (application.getStatus() != ApplicationStatus.BOOKED) throw new Exception("Application is not booked");
        String receipt = "151";
        return receipt;
    }

    // update status of BTO applicants
    public void updateStatus(Application application, ApplicationStatus status) throws Exception {
        if (application.getStatus() == ApplicationStatus.BOOKED) throw new Exception("Cannot update booked application status");
        application.setStatus(status);
        applicationDao.save(application);

        if (status == ApplicationStatus.BOOKED) {
            projectDao.decreaseAvailableUnits(application.getProjectName(), application.getFlatType(), 1);
        }
    }

    
    // view all enquiries for officer assigned project
    public List<Enquiry> viewEnquiriesForMyProject() throws Exception {
        if (officer.getProject() == null) throw new Exception("Officer is not assigned to any project");
        return enquiryDao.getEnquiriesByProject(officer.getProject().getName());
    }

    // Officer can reply to an enquiry for their project
    public void replyEnquiry(int enquiryId, String reply) throws Exception{
        Enquiry enquiry = enquiryDao.findById(enquiryId);
        if (enquiry == null) throw new Exception("Enquiry not found");
        if (officer.getProject() == null || !enquiry.getProjectName().equals(officer.getProject().getName()))
            throw new Exception("You are not assigned to this project");
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


}
