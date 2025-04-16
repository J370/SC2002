package bto.Controller;

import bto.Model.*;
import bto.Data.*;

public class AuthController {
    public boolean validateLogin(User user, String password) {
        if (user != null) {
            // Check if the password matches
            if (user.getPassword().equals(password)) {
                return true; // Login successful
            } else {
                return false; // Incorrect password
            }
        } else {
            return false; // User not found
        }
    }

    public void changePassword(User user, String newPassword) {
        if (user != null) {
            user.setPassword(newPassword);
            UserCSVDao userDao = new UserCSVDao();
            userDao.updateUser(user);
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("User not found.");
        }
    }
}
