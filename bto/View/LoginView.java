package bto.View;

import bto.Controller.*;
import bto.Data.ApplicationCSVDao;
import bto.Data.ApplicationDao;
import bto.Data.EnquiryCSVDao;
import bto.Data.EnquiryDao;
import bto.Data.ProjectCSVDao;
import bto.Data.ProjectDao;
import bto.Model.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * View class for managing the login process in the BTO Application System.
 * This class handles user authentication and redirects users to their respective views based on their roles.
 */
public class LoginView {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the login prompt to the user and handles the login process.
     * Validates the user's NRIC and password, and redirects them to the appropriate view based on their role.
     */
    public void displayLoginPrompt() {
        System.out.println("\n--------------------------------------");
        System.out.println("Welcome to the BTO Application System!");
        System.out.print("Please enter your NRIC: ");
        String nric = scanner.nextLine();

        if (!isValidNric(nric)) {
            System.out.println("Invalid NRIC format. Please try again.");
            displayLoginPrompt(); 
            return;
        }

        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();

        AuthController authController = new AuthController();
        User user = User.getUser(nric);
        if (user == null) {
            System.out.println("User not found. Please try again.");
            displayLoginPrompt();
            return;            
        }
        boolean isValid = authController.validateLogin(user, password);
        if (isValid) {
            ApplicationDao applicationDao = new ApplicationCSVDao();
            ProjectDao projectDao = new ProjectCSVDao();
            EnquiryDao enquiryDao = new EnquiryCSVDao();
            if (user instanceof Applicant) {
                ApplicantController applicantController = new ApplicantController((Applicant)user, applicationDao, projectDao, enquiryDao);
                ApplicantView applicantView = new ApplicantView((Applicant)user, applicantController);
                applicantView.menu(true);
            }
            else if (user instanceof Officer) {
                OfficerController officerController = new OfficerController((Officer)user, applicationDao, projectDao, enquiryDao);
                ApplicantController applicantController = new ApplicantController((User)user, applicationDao, projectDao, enquiryDao);
                ApplicantView applicantView = new ApplicantView((User)user, applicantController);
                OfficerView officerView = new OfficerView((Officer)user, officerController, applicantController, applicantView);
                officerView.menu(true);
            }
            else if (user instanceof Manager) {
                ManagerController managerController = new ManagerController((Manager)user, projectDao, applicationDao, enquiryDao);
                ManagerView managerView = new ManagerView((Manager)user, managerController);
                managerView.menu(true);
            }
            else {
                System.out.println("Invalid user type.");
            }
        } else {
            System.out.println("Invalid password. Please try again.");
            displayLoginPrompt(); 
        }
    }

    /**
     * Validates the format of the provided NRIC.
     *
     * @param nric The NRIC to validate.
     * @return {@code true} if the NRIC format is valid, {@code false} otherwise.
     */
    private boolean isValidNric(String nric) {
        String nricPattern = "^[ST]\\d{7}[A-Z]$";
        Pattern pattern = Pattern.compile(nricPattern);
        Matcher matcher = pattern.matcher(nric);
        return matcher.matches();
    }
}
