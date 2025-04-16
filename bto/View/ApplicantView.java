package bto.View;

import java.util.Scanner;
import bto.Controller.*;
import bto.Model.*;
import bto.Data.*;
import java.util.List;

public class ApplicantView extends UserView {
    ApplicantController applicantController;

    public ApplicantView(Applicant applicant) {
        ApplicationDao applicationDao = new ApplicationCSVDao();
        ProjectDao projectDao = new ProjectCSVDao();
        EnquiryDao enquiryDao = new EnquiryCSVDao();

        this.applicantController = new ApplicantController(applicant, applicationDao, projectDao, enquiryDao);
    }

    Scanner scanner = new Scanner(System.in);

    public void menu() {
        System.out.println("\nYou have successfully logged in!");
        System.out.println("1. Change password");
        System.out.println("2. View projects");
        System.out.println("3. Apply for projects");
        System.out.println("4. View application status");
        System.out.println("5. Withdraw application");
        System.out.println("6. Make an enquiry");
        System.out.println("7. View enquiry status");
        System.out.println("8. Edit enquiry");
        System.out.println("9. Delete enquiry");
        
        System.out.print("Please select an option: ");

        switch (scanner.nextInt()) {
            case 1:
                menu();

            case 2:
                applicantController.viewAvailableProjects();
                menu();

            case 3:
                System.out.print("Enter the project name you want to apply for: ");
                String projectName = scanner.next();
                applicantController.applyProject(projectName);
                menu();
            
            case 4:
                Application application = applicantController.viewApplication();
                System.out.println("Application Project Name: " + application.getProjectName());
                System.out.println("Application Status: " + application.getStatus());
                menu();
            
            case 5:
                System.out.println("Are you sure you want to withdraw: ");
                System.out.println("1. Yes");
                System.out.println("2. No");
                boolean confirm = scanner.nextInt() == 1;
                if (!confirm) {
                    System.out.println("Withdrawal cancelled.");
                    menu();
                }
                else {
                    applicantController.withdrawApplication();
                    System.out.println("Application withdrawn successfully.");
                    menu();
                }

            case 6:
                System.out.print("Enter the project name for your enquiry: ");
                String projectNameEnquiry = scanner.next();
                System.out.print("Enter your enquiry details: ");
                String enquiryDetails = scanner.next();
                applicantController.submitEnquiry(projectNameEnquiry, enquiryDetails);
                System.out.println("Enquiry submitted successfully.");
                menu();

            case 7:
                List<Enquiry> userEnquiries = applicantController.viewEnquiries();
                if (userEnquiries.isEmpty()) {
                    System.out.println("No enquiries found.");
                } else {
                    for (Enquiry enquiry : userEnquiries) {
                        System.out.println("Enquiry ID: " + enquiry.getId());
                        System.out.println("Project Name: " + enquiry.getProjectName());
                        System.out.println("Enquiry Details: " + enquiry.getDetails());
                        System.out.println("Reply: " + (enquiry.getReply() != null ? enquiry.getReply() : "No reply yet"));
                    }
                }
                menu();
            
            case 8:
                System.out.print("Enter the enquiry ID you want to edit: ");
                int enquiryId = scanner.nextInt();
                System.out.print("Enter the new details: ");
                String newDetails = scanner.next();
                applicantController.editEnquiry(enquiryId, newDetails);
                System.out.println("Enquiry updated successfully.");
                menu();

            case 9:
                System.out.print("Enter the enquiry ID you want to delete: ");
                int deleteEnquiryId = scanner.nextInt();
                System.out.println("Are you sure you want to delete this enquiry? (1 for Yes, 2 for No)");
                if (scanner.nextInt() == 1) {
                    applicantController.deleteEnquiry(deleteEnquiryId);
                    System.out.println("Enquiry deleted successfully.");
                } else {
                    System.out.println("Deletion cancelled.");
                }
                menu();

            default:
        }
    }

    public void displayProjects(List<Project> projects, String maritalStatus) {
        System.out.println("Available Projects:");
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }
}
