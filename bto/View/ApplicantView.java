package bto.View;

import java.util.Scanner;
import bto.Controller.*;

public class ApplicantView extends UserView {
    Scanner scanner = new Scanner(System.in);
    ApplicantController applicantController = new ApplicantController();

    public void menu() {
        System.out.println("\nYou have successfully logged in!");
        System.out.println("1. Change password");
        System.out.println("2. View projects");
        System.out.println("3. Apply for projects");
        System.out.print("Please select an option: ");

        switch (scanner.nextInt()) {
            case 1:
                applicantController.viewAvailableProjects();
                menu();

            case 2:
                menu();
        
            default:
                menu();
        }
    }
}
