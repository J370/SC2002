package bto.View;

import java.util.Scanner;

import bto.Controller.*;
import bto.Model.*;

/**
 * The UserView class serves as a base class for user-related views.
 * It provides common functionality such as changing the user's password.
 */
public class UserView {

    private User user;
    Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a UserView instance.
     *
     * @param user The user associated with this view.
     */
    public UserView(User user) {
        this.user = user;
    }

    /**
     * Allows the user to change their password.
     * Prompts the user to enter a new password and updates it using the AuthController.
     */
    public void changePassword() {
        AuthController authController = new AuthController();
        System.out.print("Enter your new password: ");
        String newPassword = scanner.next();
        authController.changePassword(user, newPassword);
    }
}
