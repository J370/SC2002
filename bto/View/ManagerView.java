package bto.View;

import bto.Controller.*;
import bto.Model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
        System.out.println("4. Edit project");
        System.out.println("5. View all projects");
        System.out.println("6. Logout");
        System.out.print("Please select an option: ");

        int option = scanner.nextInt();
        scanner.nextLine(); // Consume leftover newline

        switch (option) {
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
                editProject();
                menu(false);
                break;

            case 5:
                generateReport();
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
        scanner.nextLine(); // Consume any leftover newline
    
        System.out.print("Please enter the project name: ");
        String name = scanner.nextLine().trim();
    
        System.out.print("Please enter the neighborhood: ");
        String neighborhood = scanner.nextLine().trim();
    
        System.out.print("Please enter the opening date (yyyy/MM/dd): ");
        String openingDateInput = scanner.nextLine().trim();
        LocalDate openingDateStr = LocalDate.parse(openingDateInput, dateFormatter);
    
        System.out.print("Please enter the closing date (yyyy/MM/dd): ");
        String closingDateInput = scanner.nextLine().trim();
        LocalDate closingDateStr = LocalDate.parse(closingDateInput, dateFormatter);
    
        System.out.print("Please enter the officer slots: ");
        int officerSlots = Integer.parseInt(scanner.nextLine().trim());
    
        Map<String, Project.FlatTypeDetails> flatTypes = new HashMap<>();
        String[] allowedTypes = {"2-Room", "3-Room"};
        for (String flatType : allowedTypes) {
            System.out.println("Enter details for " + flatType + ":");
            System.out.print("Number of units: ");
            int numberOfUnits = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            flatTypes.put(flatType, new Project.FlatTypeDetails(numberOfUnits, price));
        }
    
        try {
            managerController.createProject(name, neighborhood, flatTypes, openingDateStr, closingDateStr, officerSlots);
            System.out.println("Project created successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the project: " + e.getMessage());
        }
    }

    public void deleteProject() {
        scanner.nextLine(); // Consume any leftover newline
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

    public void editProject() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        scanner.nextLine(); // consume leftover newline if needed
    
        System.out.print("Please enter the project name to edit: ");
        String projectName = scanner.nextLine().trim();
        if (projectName.isEmpty()) {
            System.out.println("Project name is required to edit a project.");
            return;
        }
    
        Project project = managerController.viewAllProject().stream()
            .filter(p -> p.getName().equals(projectName))
            .findFirst().orElse(null);
    
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }
    
        System.out.println("Press Enter to keep the current value.");
    
        System.out.print("Current neighborhood: " + project.getNeighborhood() + ". New neighborhood (or press Enter to skip): ");
        String neighborhood = scanner.nextLine().trim();
        if (neighborhood.isEmpty()) neighborhood = project.getNeighborhood();
    
        System.out.print("Current opening date: " + project.getOpeningDate() + ". New opening date (yyyy/MM/dd, or press Enter to skip): ");
        String openingDateStr = scanner.nextLine().trim();
        LocalDate openingDate = project.getOpeningDate();
        if (!openingDateStr.isEmpty()) {
            try {
                openingDate = LocalDate.parse(openingDateStr, dateFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Keeping current opening date.");
            }
        }
    
        System.out.print("Current closing date: " + project.getClosingDate() + ". New closing date (yyyy/MM/dd, or press Enter to skip): ");
        String closingDateStr = scanner.nextLine().trim();
        LocalDate closingDate = project.getClosingDate();
        if (!closingDateStr.isEmpty()) {
            try {
                closingDate = LocalDate.parse(closingDateStr, dateFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Keeping current closing date.");
            }
        }
    
        System.out.print("Current officer slots: " + project.getOfficerSlots() + ". New officer slots (or press Enter to skip): ");
        String officerSlotsStr = scanner.nextLine().trim();
        int officerSlots = project.getOfficerSlots();
        if (!officerSlotsStr.isEmpty()) {
            try {
                officerSlots = Integer.parseInt(officerSlotsStr);
            } catch (Exception e) {
                System.out.println("Invalid number. Keeping current officer slots.");
            }
        }
    
        // Only allow editing of 2-Room and 3-Room
        Map<String, Project.FlatTypeDetails> flatTypes = new HashMap<>(project.getFlatTypes());
        String[] allowedTypes = {"2-Room", "3-Room"};
        for (String type : allowedTypes) {
            Project.FlatTypeDetails details = flatTypes.get(type);
            if (details == null) {
                // If missing, add with default values
                details = new Project.FlatTypeDetails(0, 0.0);
            }
            System.out.print("Current " + type + " units: " + details.getAvailableUnits() + ". New units (or press Enter to skip): ");
            String unitsStr = scanner.nextLine().trim();
            int units = details.getAvailableUnits();
            if (!unitsStr.isEmpty()) {
                try { units = Integer.parseInt(unitsStr); } catch (Exception e) {}
            }
            System.out.print("Current " + type + " price: " + details.getSellingPrice() + ". New price (or press Enter to skip): ");
            String priceStr = scanner.nextLine().trim();
            double price = details.getSellingPrice();
            if (!priceStr.isEmpty()) {
                try { price = Double.parseDouble(priceStr); } catch (Exception e) {}
            }
            flatTypes.put(type, new Project.FlatTypeDetails(units, price));
        }
    
        // Ensure only 2-Room and 3-Room exist
        flatTypes.keySet().retainAll(java.util.Arrays.asList(allowedTypes));
    
        try {
            managerController.editProject(
                project.getName(),
                neighborhood,
                flatTypes,
                openingDate,
                closingDate,
                officerSlots
            );
            System.out.println("Project updated successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while editing the project: " + e.getMessage());
        }
    }

    public void generateReport() {
        List<Application> applications = managerController.generateReport();
    
        // Prompt for filters
        System.out.print("Enter marital status to filter by (or press Enter to skip): ");
        String maritalStatus = scanner.nextLine().trim();
        if (maritalStatus.isEmpty()) maritalStatus = null;
    
        System.out.print("Enter flat type to filter by (or press Enter to skip): ");
        String flatType = scanner.nextLine().trim();
        if (flatType.isEmpty()) flatType = null;
    
        System.out.print("Enter project name to filter by (or press Enter to skip): ");
        String projectName = scanner.nextLine().trim();
        if (projectName.isEmpty()) projectName = null;
    
        System.out.println("\n===== Filtered BTO Booking Report =====");
        boolean found = false;
        for (Application app : applications) {
            User applicant = User.getUser(app.getApplicantNric());
            if (applicant == null) continue;
    
            // Apply filters
            if (maritalStatus != null && !applicant.getMaritalStatus().equalsIgnoreCase(maritalStatus)) continue;
            if (flatType != null && !app.getFlatType().equalsIgnoreCase(flatType)) continue;
            if (projectName != null && !app.getProjectName().equalsIgnoreCase(projectName)) continue;
    
            Project project = managerController.viewAllProject().stream()
                .filter(p -> p.getName().equals(app.getProjectName()))
                .findFirst().orElse(null);
            if (project == null) continue;
    
            Project.FlatTypeDetails flatDetails = project.getFlatTypes().get(app.getFlatType());
            String flatPrice = (flatDetails != null) ? String.valueOf(flatDetails.getSellingPrice()) : "N/A";
    
            System.out.println("-------------------------------");
            System.out.println("Applicant Name: " + applicant.getName());
            System.out.println("NRIC: " + applicant.getNric());
            System.out.println("Age: " + applicant.getAge());
            System.out.println("Marital Status: " + applicant.getMaritalStatus());
            System.out.println("Flat Type Booked: " + app.getFlatType());
            System.out.println("Project Name: " + project.getName());
            System.out.println("Neighborhood: " + project.getNeighborhood());
            System.out.println("Flat Price: $" + flatPrice);
            System.out.println("Booking Status: " + app.getStatus());
            System.out.println("-------------------------------");
            found = true;
        }
        if (!found) {
            System.out.println("No applications found matching the filter criteria.");
        }
        System.out.println("===== End of Report =====");
    }
}
