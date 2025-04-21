package bto.View;

import bto.Controller.*;
import bto.Data.*;
import bto.Model.*;
import java.util.List;

public class OfficerView extends UserView {
    OfficerController officerController;
    Officer officer;

    public OfficerView(Officer officer) {
        super(officer);
        ApplicationDao applicationDao = new ApplicationCSVDao();
        ProjectDao projectDao = new ProjectCSVDao();
        EnquiryDao enquiryDao = new EnquiryCSVDao();

        this.officerController = new OfficerController(officer, applicationDao, projectDao, enquiryDao);
        this.officer = officer;
    }
    
    public void menu(boolean isFirstTime) {
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
        System.out.println("8. Has applied to project");
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

            // case 5:
            //     updateStatus();
            //     menu(false);
            //     break;

            // case 6:
            //     viewEnquiriesForMyProject();
            //     menu(false);
            //     break;

            // case 7:
            //     replyToEnquiry();
            //     menu(false);
            //     break;

            // case 8:
            //     hasAppliedToProject();
            //     menu(false);
            //     break;

            case 9:
                System.out.println("Logging out...");
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
        List<Project> projects = officerController.viewRegistrationStatus();
        if (projects.isEmpty()) {
            System.out.println("No registration status available.");
        } else {
            System.out.println("Registration Status:");
            for (Project project : projects) {
                System.out.println("-------------------------------");
                System.out.println("Project Name: " + project.getName());
                System.out.println("Requested Officers: " + project.getRequestedOfficers());
                System.out.println("Assigned Officers: " + project.getAssignedOfficers());
            }
        }
    }

    // public void generateReceipt() {
    //     System.out.print("Enter application ID to generate receipt: ");
    //     String applicationId = scanner.next();
    //     try {
    //         Application application = officerController.getApplicationById(applicationId);
    //         String receipt = officerController.generateReceipt(application);
    //         System.out.println("Receipt generated: " + receipt);
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //     }
    // }
}
