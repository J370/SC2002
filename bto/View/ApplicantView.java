package bto.View;

import bto.Controller.*;
import bto.Model.*;
import bto.Data.*;
import java.util.List;
import java.util.Map;

public class ApplicantView extends UserView {
    ApplicantController applicantController;
    Applicant applicant;

    public ApplicantView(Applicant applicant) {
        super(applicant);
        ApplicationCSVDao applicationDao = new ApplicationCSVDao();
        ProjectCSVDao projectDao = new ProjectCSVDao();
        EnquiryCSVDao enquiryDao = new EnquiryCSVDao();

        this.applicantController = new ApplicantController(applicant, applicationDao, projectDao, enquiryDao);
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
        System.out.println("4. View application status");
        System.out.println("5. Withdraw application");
        System.out.println("6. Make an enquiry");
        System.out.println("7. View enquiry status");
        System.out.println("8. Edit enquiry");
        System.out.println("9. Delete enquiry");
        System.out.println("10. Logout");
        System.out.print("Please select an option: ");

        switch (scanner.nextInt()) {
            case 1:
                changePassword();
                menu(false);

            case 2:
                viewProjects();
                menu(false);

            case 3:
                applyProject();
                menu(false);

            case 4:
                applicationStatus();
                menu(false);

            case 5:
                withdrawApplication();
                menu(false);

            case 6:
                makeEnquiry();
                menu(false);

            case 7:
                viewEnquiry();
                menu(false);

            case 8:
                editEnquiry();
                menu(false);

            case 9:
                deleteEnquiry();
                menu(false);
            case 10:
                System.out.println("Logging out...");
                break;

            default:
                menu(false);
        }
    }

    public void viewProjects() {
        List<Project> projects = applicantController.getAvailableProjects();
        if (projects.isEmpty()) System.out.println("No available projects."); 
        else {
            System.out.println("Available Projects:");
            for (Project project : projects) {
                System.out.println("---------------------------------");
                System.out.println("Project Name: " + project.getName());
                System.out.println("Neighborhood: " + project.getNeighborhood());

                // Print all flat types from the map
                for (Map.Entry<String, Project.FlatTypeDetails> entry : project.getFlatTypes().entrySet()) {
                    String flatType = entry.getKey();
                    Project.FlatTypeDetails details = entry.getValue();
                    System.out.println("Flat Type: " + flatType);
                    System.out.println("    Number of units: " + details.getAvailableUnits());
                    System.out.println("    Selling price: $" + details.getSellingPrice());
                }

                System.out.println("Application opening date: " + project.getOpeningDate());
                System.out.println("Application closing date: " + project.getClosingDate());
                System.out.println("Manager: " + project.getManager());
                System.out.println("Officer(s): " + String.join(", ", project.getAssignedOfficers()));
                System.out.println("---------------------------------");
            }
        }
    }

    public void applyProject() {
        System.out.print("Enter the project name you want to apply for: ");
        scanner.nextLine();
        String projectName = scanner.nextLine();
        try {
            applicantController.applyProject(projectName);
            System.out.println("Application submitted successfully.");
        } 
        catch (Exception e) {System.out.println("Error: " + e.getMessage());}
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
        System.out.print("Enter the new details: ");
        scanner.nextLine();
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
        System.out.println("Are you sure you want to delete this enquiry?\n(1 for Yes, 2 for No)");
        if (scanner.nextInt() == 1) {
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
