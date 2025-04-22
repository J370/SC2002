package bto.View;

import bto.Controller.*;
import bto.Model.*;
import java.util.List;
import java.util.Map;

public class ApplicantView extends UserView {
    ApplicantController applicantController;
    Applicant applicant;

    public ApplicantView(Applicant applicant, ApplicantController applicantController) {
        super(applicant);
        this.applicantController = applicantController;
        this.applicant = applicant;
    }

    public void menu(boolean isFirstTime) {
        if (isFirstTime) {
            System.out.println("Welcome " + applicant.getName() + "!");
            System.out.println("You are logged in as an applicant.");
        } else {
            System.out.println("Welcome back " + applicant.getName() + "!");
        }
        System.out.println("1. Change password");
        System.out.println("2. View projects");
        System.out.println("3. Apply for projects");
        System.out.println("4. View all my applications");
        System.out.println("5. View active application status");
        System.out.println("6. Withdraw application");
        System.out.println("7. Make an enquiry");
        System.out.println("8. View enquiry status");
        System.out.println("9. Edit enquiry");
        System.out.println("10. Delete enquiry");
        System.out.println("11. Logout");
        System.out.print("Please select an option: ");
    
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (option) {
            case 1:
                changePassword();
                menu(false);
                break;
            case 2:
                viewProjects();
                menu(false);
                break;
            case 3:
                applyProject();
                menu(false);
                break;
            case 4:
                viewMyApplications();
                menu(false);
                break;
            case 5:
                applicationStatus();
                menu(false);
                break;
            case 6:
                withdrawApplication();
                menu(false);
                break;
            case 7:
                makeEnquiry();
                menu(false);
                break;
            case 8:
                viewEnquiry();
                menu(false);
                break;
            case 9:
                editEnquiry();
                menu(false);
                break;
            case 10:
                deleteEnquiry();
                menu(false);
                break;
            case 11:
                System.out.println("Logging out...");
                break;
            default:
                menu(false);
        }
    }

    public void viewProjects() {
        List<Project> projects = applicantController.getAvailableProjects();

        if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() < 35) {
            System.out.println("You're single and under 35, no BTO are available for you.");
            return;
        }
        if (projects.isEmpty()) {
            System.out.println("No available projects.");
        } else {
            System.out.println("Available Projects:");
            for (Project project : projects) {
                System.out.println("-------------------------------");
                System.out.println("Project Name: " + project.getName());
                System.out.println("Neighborhood: " + project.getNeighborhood());
    
                if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() >= 35) {
                    Project.FlatTypeDetails details = project.getFlatTypes().get("2-Room");
                    if (details != null) {
                        System.out.println("Flat Type: 2-Room");
                        System.out.println("    Number of units: " + details.getAvailableUnits());
                        System.out.println("    Selling price: $" + details.getSellingPrice());
                    }
                } else {
                    for (Map.Entry<String, Project.FlatTypeDetails> entry : project.getFlatTypes().entrySet()) {
                        String flatType = entry.getKey();
                        Project.FlatTypeDetails details = entry.getValue();
                        System.out.println("Flat Type: " + flatType);
                        System.out.println("    Number of units: " + details.getAvailableUnits());
                        System.out.println("    Selling price: $" + details.getSellingPrice());
                    }
                }
    
                System.out.println("Application opening date: " + project.getOpeningDate());
                System.out.println("Application closing date: " + project.getClosingDate());
                System.out.println("Manager: " + project.getManager());
                System.out.println("Officer(s): " + String.join(", ", project.getAssignedOfficers()));
                System.out.println("-------------------------------");
            }
        }
    }

    public void applyProject() {
        System.out.print("Enter the project name you want to apply for: ");
        String projectName = scanner.nextLine();
    
        System.out.print("Enter the flat type you want to apply for(2-Room/3-Room): ");
        String flatType = scanner.nextLine();
    
        try {
            applicantController.applyProject(projectName, flatType);
            System.out.println("Application submitted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void viewMyApplications() {
        List<Application> apps = applicantController.getAllApplications();
        for (Application app : apps) {
            System.out.println("-------------------------------");
            System.out.println("Application ID: " + app.getId());
            System.out.println("Project Name: " + app.getProjectName());
            System.out.println("Flat Type: " + app.getFlatType());
            System.out.println("Status: " + app.getStatus());
            System.out.println("Created Time: " + (app.getCreatedTime() != null ? app.getCreatedTime().toString() : "NIL"));
            System.out.println("-------------------------------");
        }
    }

    public void applicationStatus() {
        try{        
            Application application = applicantController.viewActiveApplication();
            System.out.println("Application Project Name: " + application.getProjectName());
            System.out.println("Application Status: " + application.getStatus());
        }
        catch (Exception e) {System.out.println("Error: " + e.getMessage());}

    }

    public void withdrawApplication() {
        System.out.println("Are you sure you want to withdraw?\n(1 for Yes, 2 for No)");
        boolean confirm = scanner.nextInt() == 1;
        scanner.nextLine();
        if (!confirm) {
            System.out.println("Withdrawal cancelled.");
        } else {
            try{
                applicantController.withdrawApplication();
                System.out.println("Application withdrawn successfully.");
            }
            catch (Exception e) {System.out.println("Error: " + e.getMessage());}
        }
    }

    public void makeEnquiry() {
        System.out.print("Enter the project name for your enquiry: ");
        scanner.nextLine();
        String projectNameEnquiry = scanner.nextLine();
        System.out.print("Enter your enquiry details: ");
        String enquiryDetails = scanner.nextLine();
        applicantController.submitEnquiry(projectNameEnquiry, enquiryDetails);
        System.out.println("Enquiry submitted successfully.");
    }

    public void viewEnquiry() {
        List<Enquiry> userEnquiries = applicantController.viewEnquiries();
        if (userEnquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            for (Enquiry enquiry : userEnquiries) {
                System.out.println("---------------------------------");
                System.out.println("Enquiry ID: " + enquiry.getId());
                System.out.println("Project Name: " + enquiry.getProjectName());
                System.out.println("Enquiry Details: " + enquiry.getDetails());
                System.out.println("Created Time: " + (enquiry.getCreatedTime() != null ? enquiry.getCreatedTime().toString() : "NIL"));
                System.out.println("Reply: " + (enquiry.getReply() != null ? enquiry.getReply() : "No reply yet"));
                System.out.println("Replied By: " + (enquiry.getRepliedBy() != null ? enquiry.getRepliedBy() : "NIL"));
                System.out.println("Replied Time: " + (enquiry.getRepliedTime() != null ? enquiry.getRepliedTime().toString() : "NIL"));
                System.out.println("---------------------------------");
            }
        }
    }

    public void editEnquiry() {
        System.out.print("Enter the enquiry ID you want to edit: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new details: ");
        String newDetails = scanner.nextLine();
        try{
            applicantController.editEnquiry(enquiryId, newDetails);
            System.out.println("Enquiry updated successfully.");
        }
        catch (Exception e) {System.out.println("Error: " + e.getMessage());}
    }

    public void deleteEnquiry() {
        System.out.print("Enter the enquiry ID you want to delete: ");
        int deleteEnquiryId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Are you sure you want to delete this enquiry?\n(1 for Yes, 2 for No)");
        int confirm = scanner.nextInt();
        scanner.nextLine();
        if (confirm == 1) {
            try{
                applicantController.deleteEnquiry(deleteEnquiryId);
                System.out.println("Enquiry deleted successfully.");
            }
            catch (Exception e) {System.out.println("Error: " + e.getMessage());}
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public void displayProjects(List<Project> projects, String maritalStatus) {
        System.out.println("Available Projects:");
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }
}
