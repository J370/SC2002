package bto.View;

import java.util.Scanner;

import bto.Controller.*;
import bto.Model.*;

public class UserView {
    private User user;
    Scanner scanner = new Scanner(System.in);

    public UserView(User user) {
        this.user = user;
    }

    public void changePassword() {
        AuthController authController = new AuthController();
        System.out.print("Enter your new password: ");
        String newPassword = scanner.next();
        authController.changePassword(user, newPassword);
    }
}
