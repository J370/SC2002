package bto.Controller;

import bto.Model.*;
import bto.Data.*;

/**
 * Controller class for handling authentication-related operations.
 */
public class AuthController {

    /**
     * Validates the login credentials of a user.
     *
     * @param user The user attempting to log in.
     * @param password The password provided by the user.
     * @return {@code true} if the credentials are valid, {@code false} otherwise.
     */
    public boolean validateLogin(User user, String password) {
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return true; 
            } else {
                return false; 
            }
        } else {
            return false; 
        }
    }

    /**
     * Changes the password of a user.
     *
     * @param user The user whose password is to be changed.
     * @param newPassword The new password to set.
     */
    public void changePassword(User user, String newPassword) {
        if (user != null) {
            if (user.getPassword().equals(newPassword)) {
                System.out.println("New password cannot be the same as the old password.");
                return;
            }
            user.setPassword(newPassword);
            UserCSVDao userDao = new UserCSVDao();
            userDao.updateUser(user);
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("User not found.");
        }
    }
}
