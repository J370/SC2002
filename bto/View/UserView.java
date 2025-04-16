package bto.View;

import java.util.Scanner;

public class UserView {
    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your new password: ");
        String newPassword = scanner.next();
        System.out.println("Password changed successfully!");
    }
}
