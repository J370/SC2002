package bto;

import java.util.Scanner;

public class App {
    public App() {

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isAuthenticated = false;

        CSV csv = new CSV();
        csv.readUser("ApplicantList.csv");
        csv.readUser("ManagerList.csv");
        csv.readUser("OfficerList.csv");

        System.out.println("Welcome to the BTO Application System!");
        System.out.println("Please enter your NRIC: ");
        String nric = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
    }
}
