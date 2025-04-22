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
                OfficerView officerView = new OfficerView((Officer)user, officerController);
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
            System.out.println("Invalid NRIC or password. Please try again.");
            displayLoginPrompt(); // Retry login
        }
    }
}
