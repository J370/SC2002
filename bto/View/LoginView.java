package bto.View;
import bto.Controller.*;

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
        boolean isValid = authController.validateLogin(nric, password);
        if (isValid) {
            mainMenu();
            // Proceed to the next step, e.g., displaying the main menu
        } else {
            System.out.println("Invalid NRIC or password. Please try again.");
            displayLoginPrompt(); // Retry login
        }
    }

    public void mainMenu() {
        System.out.println("\nYou have successfully logged in!");
        System.out.println("1. Change password");
        System.out.println("Please select an option:");

        switch (scanner.nextInt()) {
            case 1:
                changePassword();
                break;
        
            default:
                break;
        }
    }

    public void changePassword() {
        System.out.print("Please enter your new password: ");
        String newPassword = scanner.next();
        // Here you would typically call a method to update the password in the database
        System.out.println("Password changed successfully!");
        mainMenu(); // Return to main menu after changing password
    }
}
