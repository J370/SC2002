package bto.View;

import bto.Controller.*;
import bto.Model.*;
import java.util.List;

public class OfficerView extends UserView {
    OfficerController officerController;
    Officer officer;
    ApplicantController applicantController;
    ApplicantView applicantView;

    public OfficerView(Officer officer, OfficerController officerController, ApplicantController applicantController, ApplicantView applicantView) {
        super(officer);
        this.officer = officer;
        this.officerController = officerController;
        this.applicantController = applicantController;
        this.applicantView = applicantView;
    }
    
    public void menu(boolean isFirstTime) {
        System.out.println("\n===================================");
        if (isFirstTime) {
            System.out.println("Welcome " + officer.getName() + "!");
            System.out.println("You are logged in as an officer.");
        } else {
            System.out.println("Welcome back " + officer.getName() + "!");
        }
        System.out.println("1. Change password");
        System.out.println("2. Register projects");
        System.out.println("3. View registration status");
        System.out.println("4. Generate receipt");
        System.out.println("5. Update status");
        System.out.println("6. View enquiries for my project");
        System.out.println("7. Reply to enquiry");
        System.out.println("8. View as applicant");
        System.out.println("9. Logout");
        System.out.print("Please select an option: ");

        switch (scanner.nextInt()) {
            case 1:
                changePassword();
                menu(false);
                break;

            case 2:
                registerProject();
                menu(false);
                break;

            case 3:
                viewRegistrationStatus();
                menu(false);
                break;

            // case 4:
            //     generateReceipt();
            //     menu(false);
            //     break;

            case 5:
                updateStatus();
                menu(false);
                break;

            case 6:
                viewEnquiriesForMyProject();
                menu(false);
                break;

            case 7:
                replyToEnquiry();
                menu(false);
                break;

            case 8:
                applicantView.menu(true);
                menu(false);
                break;

            case 9:
                System.out.println("Logging out...");
                LoginView loginView = new LoginView();
                loginView.displayLoginPrompt();
                break;
            
            default:
            menu(false);
        }
    }

    public void registerProject() {
        System.out.print("Enter project name to register: ");
        scanner.nextLine();
        String projectName = scanner.nextLine();
        try {
            officerController.registerProject(projectName);
            System.out.println("Successfully registered for project: " + projectName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewRegistrationStatus() {
        List<OfficerController.RegistrationStatus> status = officerController.viewRegistrationStatus();
        if (status.isEmpty()) {
            System.out.println("No registration status available.");
        } else {
            System.out.println("Registration Status:");
            for (OfficerController.RegistrationStatus s : status) {
                System.out.println("-----------------------------");
                System.out.println("Project Name: " + s.project.getName());
                System.out.println("Application Status: " + s.status);
            }
        }
    }

    // public void generateReceipt() {
    //     System.out.print("Enter application ID to generate receipt: ");
    //     String applicationId = scanner.next();
    //     try {
    //         Application application = applicationDao.getApplicationById(applicationId);
    //         String receipt = officerController.generateReceipt(application);
    //         System.out.println("Receipt generated: " + receipt);
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //     }
    // }

    public void updateStatus() {
        try {
            List<Application> applications = officerController.viewApplicationsForMyProjects();
            System.out.println("Applications:");
            for (Application application : applications) {
                System.out.println("-------------------------------");
                System.out.println("Application ID: " + application.getId());
                System.out.println("Applicant NRIC: " + application.getApplicantNric());
                System.out.println("Project Name: " + application.getProjectName());
                System.out.println("Status: " + application.getStatus());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Enter successful application ID to update status to booked: ");
        scanner.nextLine();
        String applicationId = scanner.nextLine();

        try {
            officerController.updateStatus(applicationId);
            System.out.println("Status updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewEnquiriesForMyProject() {
        try {
            List<Enquiry> enquiries = officerController.viewEnquiriesForMyProjects();
            System.out.println("Enquiries:");
            for (Enquiry enquiry : enquiries) {
                System.out.println("-------------------------------");
                System.out.println("Enquiry ID: " + enquiry.getId());
                System.out.println("Project Name: " + enquiry.getProjectName());
                System.out.println("Enquiry Details: " + enquiry.getDetails());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void replyToEnquiry() {
        System.out.print("Enter enquiry ID to reply: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();

        try {
            officerController.replyEnquiry(enquiryId, reply);
            System.out.println("Reply sent successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
