package bto.Controller;

import bto.Model.*;
import bto.Data.*;

public class AuthController {
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
