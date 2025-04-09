package bto.View;
import bto.Controller.*;

import java.util.Scanner;

public class LoginView {
    Scanner scanner = new Scanner(System.in);

    public void displayLoginPrompt() {
        System.out.println("Welcome to the BTO Application System!");
        System.out.println("Please enter your NRIC: ");
        String nric = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
    }
}
