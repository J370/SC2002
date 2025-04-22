package bto.View;

import bto.Controller.*;
import bto.Model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ManagerView extends UserView {
    ManagerController managerController;
    Manager manager;

    public ManagerView(Manager manager, ManagerController managerController) {
        super(manager);
        this.managerController = managerController;
        this.manager = manager;
    }

    public void menu(boolean isFirstTime) {
        System.out.println("\n===================================");
        if (isFirstTime) {
            System.out.println("Welcome " + manager.getName() + "!");
            System.out.println("You are logged in as a manager.");
        } else {
            System.out.println("Welcome back " + manager.getName() + "!");
        }
        System.out.println("1. Change password");
        System.out.println("2. Create project");
        System.out.println("3. Delete project");
        System.out.println("4. Manage project");
        System.out.println("5. View all projects");
        System.out.println("6. Logout");
        System.out.print("Please select an option: ");

        switch (scanner.nextInt()) {
            case 1:
                changePassword();
                menu(false);                
                break;
            
            case 2:
                createProject();
                menu(false);
                break;

            case 3:
                deleteProject();
                menu(false);
                break;

            case 4:
                manageProject();
                menu(false);
                break;

            case 5:
                viewAllProjects();
                menu(false);
                break;

            case 6:
                System.out.println("Logging out...");
                LoginView loginView = new LoginView();
                loginView.displayLoginPrompt();
                break;
        
            default:
                break;
        }
    }

    public void createProject() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        System.out.print("Please enter the project name: ");
        String name = scanner.next();
        System.out.print("Please enter the neighborhood: ");
        String neighborhood = scanner.next();
        System.out.print("Please enter the opening date (yyyy/mm/dd): ");
        LocalDate openingDateStr = LocalDate.parse(scanner.next(), dateFormatter);
        System.out.print("Please enter the closing date (yyyy/mm/dd): ");
        LocalDate closingDateStr = LocalDate.parse(scanner.next(), dateFormatter);
        System.out.print("Please enter the officer slots: ");
        int officerSlots = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over


        System.out.println("Please enter the flat types and their details (type 'done' to finish): ");
        Map<String, Project.FlatTypeDetails> flatTypes = new HashMap<>();
        while (true) {
            System.out.println("Flat type: ");
            String flatType = scanner.next();
            if (flatType.equals("done")) {
                break;
            }
            System.out.println("Number of units: ");
            int numberOfUnits = scanner.nextInt();
            System.out.println("Price: ");
            double price = scanner.nextDouble();
            flatTypes.put(flatType, new Project.FlatTypeDetails(numberOfUnits, price));
        }
        try {
            managerController.createProject(name, neighborhood, flatTypes, openingDateStr, closingDateStr, officerSlots);
        } catch (Exception e) {
            System.out.println("An error occurred while creating the project: " + e.getMessage());
        }
    }

    public void deleteProject() {
        System.out.println("Please enter the project name to delete: ");
        String projectName = scanner.nextLine();
        System.out.print("Are you sure you want to delete the project? (yes/no)");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Project deletion cancelled.");
            return;
        }
        try {
            managerController.deleteProject(projectName);
            System.out.println("Project deleted successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the project: " + e.getMessage());
        }
    }

    public void manageProject() {
        System.out.println("Please enter the project name to manage: ");
        String projectName = scanner.nextLine();

        managerController.manageProject();
    }

    public void viewAllProjects() {
        System.out.println("All projects:");
        for (Project project : managerController.viewAllProject()) {
            System.out.println("Project Name: " + project.getName());
            System.out.println("Neighborhood: " + project.getNeighborhood());
            System.out.println("Opening Date: " + project.getOpeningDate());
            System.out.println("Closing Date: " + project.getClosingDate());
            System.out.println("_______________________________");
        }
    }
}
