package bto.View;
import bto.Controller.*;
import bto.Model.*;

import java.util.Scanner;

public class LoginView {
    Scanner scanner = new Scanner(System.in);

    public void displayLoginPrompt() {
        System.out.println("Welcome to the BTO Application System!");
        System.out.print("Please enter your NRIC: ");
        String nric = scanner.nextLine();
        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();

        AuthController authController = new AuthController();
        User user = User.getUser(nric);
        boolean isValid = authController.validateLogin(user, password);
        if (isValid) {
            if (user instanceof Applicant) {
                ApplicantView applicantView = new ApplicantView((Applicant)user);
                applicantView.menu(true);
            }
            else if (user instanceof Officer) {
                OfficerView officerView = new OfficerView((Officer)user);
                officerView.menu(true);
            }
        } else {
            System.out.println("Invalid NRIC or password. Please try again.");
            displayLoginPrompt(); // Retry login
        }
    }
}
