package bto.View;

import java.util.Scanner;

import bto.Controller.ApplicantController;
import bto.Data.ApplicationCSVDao;
import bto.Data.ApplicationDao;
import bto.Data.EnquiryCSVDao;
import bto.Data.EnquiryDao;
import bto.Data.ProjectCSVDao;
import bto.Data.ProjectDao;
import bto.Model.*;

public class UserView {
    private User user;

    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your new password: ");
        String newPassword = scanner.next();
        System.out.println("Password changed successfully!");
    }
}
