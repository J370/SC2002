package bto.Controller;

import bto.Model.*;

public class AuthController {
    public boolean validateLogin(String nric, String password) {
        User user = User.getUser(nric);
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
