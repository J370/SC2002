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
        System.out.print("Please select an option: ");

        switch (scanner.nextInt()) {
            case 1:
                menu();

            case 2:
                applicantController.viewAvailableProjects();
                menu();
        
            default:
                menu();
        }
    }

    public void displayProjects(List<Project> projects, String maritalStatus) {
        System.out.println("Available Projects:");
        for (Project project : projects) {
            System.out.println(project);
        }
    }
}
