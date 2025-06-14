package bto.View;

import bto.Controller.*;
import bto.Model.*;
import java.util.List;
import java.util.Map;

/**
 * View class for managing applicant-related interactions with the system.
 * This class provides a menu-driven interface for applicants to perform various actions,
 * such as viewing projects, submitting applications, and managing enquiries.
 */
public class ApplicantView extends UserView {
    private final ApplicantController applicantController;
    private final User applicant;

    /**
     * Constructs an ApplicantView with the specified applicant and controller.
     *
     * @param applicant The applicant associated with this view.
     * @param applicantController The controller for managing applicant-related operations.
     */
    public ApplicantView(User applicant, ApplicantController applicantController) {
        super(applicant);
        this.applicantController = applicantController;
        this.applicant = applicant;
    }

    /**
     * Displays the main menu for the applicant.
     *
     * @param isFirstTime {@code true} if this is the first time the menu is displayed, {@code false} otherwise.
     */
    public void menu(boolean isFirstTime) {
        System.out.println("\n===================================");
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
        scanner.nextLine(); 
    
        try {
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
                    LoginView loginView = new LoginView();
                    loginView.displayLoginPrompt();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    menu(false);
                    break;
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            menu(false);
        }
    }

    /**
     * Displays the list of available projects for the applicant.
     */
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
                        System.out.println("Number of units: " + details.getAvailableUnits());
                        System.out.println("Selling price: $" + details.getSellingPrice());
                    }
                } else {
                    for (Map.Entry<String, Project.FlatTypeDetails> entry : project.getFlatTypes().entrySet()) {
                        String flatType = entry.getKey();
                        Project.FlatTypeDetails details = entry.getValue();
                        System.out.println("Flat Type: " + flatType);
                        System.out.println("Number of units: " + details.getAvailableUnits());
                        System.out.println("Selling price: $" + details.getSellingPrice());
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

    /**
     * Allows the applicant to apply for a project.
     */
    public void applyProject() {
        if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() < 35) {
            System.out.println("You're single and under 35, no BTO are available for you.");
            return;
        }

        System.out.print("Enter the project name you want to apply for: ");
        String projectName = scanner.nextLine();
    
        System.out.print("Enter the flat type you want to apply for (2-Room/3-Room): ");
        String flatType = scanner.nextLine();

        if (!flatType.equals("2-Room") & !flatType.equals("3-Room")) {
            System.out.println("Invalid flat type. Please enter either 2-Room or 3-Room.");
            return;
        }

        if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() >= 35) {
            if (!flatType.equals("2-Room")) {
                System.out.println("You can only apply for 2-Room flat type.");
                return;
            }
        }
    
        try {
            applicantController.applyProject(projectName, flatType);
            System.out.println("Application submitted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all applications submitted by the applicant.
     */
    public void viewMyApplications() {
        List<Application> apps = applicantController.getAllApplications();
        if(apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        for (Application app : apps) {
            System.out.println("-------------------------------");
            System.out.println("Application ID: " + app.getId());
            System.out.println("Project Name: " + app.getProjectName());
            System.out.println("Flat Type: " + app.getFlatType());
            System.out.println("Status: " + app.getStatus());
            System.out.println("Withdrawal Requested: " + (app.getWithdrawalRequested() ? "Yes" : "No"));
            System.out.println("Created Time: " + (app.getCreatedTime() != null ? app.getCreatedTime().toString() : "NIL"));
            System.out.println("-------------------------------");
        }
    }

    /**
     * Displays the status of the active application.
     */
    public void applicationStatus() {
        try {        
            Application application = applicantController.viewActiveApplication();
            System.out.println("-------------------------------");
            System.out.println("Application Project Name: " + application.getProjectName());
            System.out.println("Application Status: " + application.getStatus());
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the applicant to request withdrawal of an active application.
     */
    public void withdrawApplication() {
        System.out.println("Are you sure you want to request withdrawal?\n(1 for Yes, 2 for No)");
        boolean confirm = scanner.nextInt() == 1;
        scanner.nextLine();
        if (!confirm) {
            System.out.println("Withdrawal cancelled.");
        } else {
            try {
                applicantController.withdrawApplication();
                System.out.println("Withdrawal request submitted. Please wait for manager approval.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Allows the applicant to submit an enquiry for a project.
     */
    public void makeEnquiry() {
        System.out.print("Enter the project name for your enquiry: ");
        String projectNameEnquiry = scanner.nextLine();
        System.out.print("Enter your enquiry details: ");
        String enquiryDetails = scanner.nextLine();
        try {
            applicantController.submitEnquiry(projectNameEnquiry, enquiryDetails);
            System.out.println("Enquiry submitted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all enquiries submitted by the applicant.
     */
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

    /**
     * Allows the applicant to edit an existing enquiry.
     */
    public void editEnquiry() {
        System.out.print("Enter the enquiry ID you want to edit: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new details: ");
        String newDetails = scanner.nextLine();
        try {
            applicantController.editEnquiry(enquiryId, newDetails);
            System.out.println("Enquiry updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the applicant to delete an existing enquiry.
     */
    public void deleteEnquiry() {
        System.out.print("Enter the enquiry ID you want to delete: ");
        int deleteEnquiryId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Are you sure you want to delete this enquiry?\n(1 for Yes, 2 for No)");
        int confirm = scanner.nextInt();
        scanner.nextLine();
        if (confirm == 1) {
            try {
                applicantController.deleteEnquiry(deleteEnquiryId);
                System.out.println("Enquiry deleted successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Displays a list of available projects for the applicant.
     *
     * @param projects A list of projects to display.
     * @param maritalStatus The marital status of the applicant, which may affect project eligibility.
     */
    public void displayProjects(List<Project> projects, String maritalStatus) {
        System.out.println("Available Projects:");
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }
}
