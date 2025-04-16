package bto.Controller;

import bto.Model.*;

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
}
