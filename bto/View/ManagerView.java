package bto.View;

import bto.Controller.*;
import bto.Model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ManagerView class provides the user interface for managers to interact with the system.
 * It allows managers to perform various actions such as creating, editing, and deleting projects,
 * managing officer registrations, handling applications, and generating reports.
 */
public class ManagerView extends UserView {
    ManagerController managerController;
    Manager manager;

    /**
     * Constructs a ManagerView with the specified manager and manager controller.
     *
     * @param manager The manager associated with this view.
     * @param managerController The controller for managing manager-related operations.
     */
    public ManagerView(Manager manager, ManagerController managerController) {
        super(manager);
        this.managerController = managerController;
        this.manager = manager;
    }

    /**
     * Displays the menu for the manager and handles user input for menu options.
     *
     * @param isFirstTime {@code true} if this is the first time the menu is displayed, {@code false} otherwise.
     */
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
        System.out.println("6. View own projects");
        System.out.println("7. View requested officer");
        System.out.println("8. Approve registration");
        System.out.println("9. Reject registration");
        System.out.println("10. Approve application");
        System.out.println("11. Reject application");
        System.out.println("12. Approve withdrawal");
        System.out.println("13. Reject withdrawal");
        System.out.println("14. View all enquiries");
        System.out.println("15. Reply to enquiry");
        System.out.println("16. Generate report");
        System.out.println("17. Toggle project visibility");
        System.out.println("18. Logout");
        System.out.print("Please select an option: ");

        int option = scanner.nextInt();

        try {
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
                    viewAllProject();
                    menu(false);
                    break;

                case 6:
                    viewOwnProjects();
                    menu(false);
                    break;

                case 7:
                    viewRequestedOfficer();
                    menu(false);
                    break;

                case 8:
                    approveRegistration();
                    menu(false);
                    break;

                case 9:
                    rejectRegistration();
                    menu(false);
                    break;

                case 10:
                    approveApplication();
                    menu(false);
                    break;
                    
                case 11:
                    rejectApplication();
                    menu(false);
                    break;

                case 12:
                    approveWithdrawal();
                    menu(false);
                    break;

                case 13:
                    rejectWithdrawal();
                    menu(false);
                    break;

                case 14:
                    viewAllEnquiries();
                    menu(false);
                    break;

                case 15:
                    replyEnquiry();
                    menu(false);
                    break;

                case 16:
                    generateReport();
                    menu(false);
                    break;

                case 17:
                    toggleProjectVisibility();
                    menu(false);
                    break;

                case 18:
                    System.out.println("Logging out...");
                    LoginView loginView = new LoginView();
                    loginView.displayLoginPrompt();
                    break;
            
                default:
                    System.out.println("Invalid option. Please try again.");
                    menu(false);
                    break;
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            menu(false);
        }
    }

    /**
     * Creates a new project.
     * Prompts the user to enter project details such as name, neighborhood, opening and closing dates, officer slots, 
     * and flat type details. The project is then created using the manager controller.
     */
    public void createProject() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        scanner.nextLine(); 
    
        System.out.print("Please enter the project name: ");
        String name = scanner.nextLine().trim();
    
        System.out.print("Please enter the neighborhood: ");
        String neighborhood = scanner.nextLine().trim();

        LocalDate openingDateStr, closingDateStr;
        System.out.print("Please enter the opening date (yyyy/MM/dd): ");
        String openingDateInput = scanner.nextLine().trim();
        openingDateStr = LocalDate.parse(openingDateInput, dateFormatter);
    
        System.out.print("Please enter the closing date (yyyy/MM/dd): ");
        String closingDateInput = scanner.nextLine().trim();
        closingDateStr = LocalDate.parse(closingDateInput, dateFormatter);
    
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an existing project.
     * Prompts the user to enter the project name and confirms the deletion before proceeding.
     *
     */
    public void deleteProject() {
        scanner.nextLine(); 
        System.out.print("Please enter the project name to delete: ");
        String projectName = scanner.nextLine();
        System.out.print("Are you sure you want to delete the project? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Project deletion cancelled.");
            return;
        }
        try {
            managerController.deleteProject(projectName);
            System.out.println("Project deleted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Edits an existing project.
     * Prompts the user to enter the project name and allows editing of project details such as neighborhood, 
     * opening and closing dates, officer slots, and flat type details. Keeps current values if no new input is provided.
     *
     */
    public void editProject() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        scanner.nextLine(); 
    
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
    
        Map<String, Project.FlatTypeDetails> flatTypes = new HashMap<>(project.getFlatTypes());
        String[] allowedTypes = {"2-Room", "3-Room"};
        for (String type : allowedTypes) {
            Project.FlatTypeDetails details = flatTypes.get(type);
            if (details == null) {
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
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all available projects in the system.
     * Shows details such as project name, neighborhood, opening and closing dates, officer slots, flat types, 
     * and visibility status.
     */
    public void viewAllProject() {
        List<Project> projects = managerController.viewAllProject();
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
        } else {
            System.out.println("Available Projects:");
            for (Project project : projects) {
                System.out.println("-----------------------------");
                System.out.println("Project Name: " + project.getName());
                System.out.println("Neighborhood: " + project.getNeighborhood());
                System.out.println("Opening Date: " + project.getOpeningDate());
                System.out.println("Closing Date: " + project.getClosingDate());
                System.out.println("Officer Slots: " + project.getOfficerSlots());
                for (Map.Entry<String, Project.FlatTypeDetails> entry : project.getFlatTypes().entrySet()) {
                    String flatType = entry.getKey();
                    Project.FlatTypeDetails details = entry.getValue();
                    System.out.println("Flat Type: " + flatType);
                    System.out.println("Available Units: " + details.getAvailableUnits());
                    System.out.println("Selling Price: $" + details.getSellingPrice());
                }
                System.out.println("Visibility: " + (project.isVisible() ? "Visible" : "Hidden"));
            }
        }
    }

    /**
     * Displays all projects managed by the current manager.
     * Shows details such as project name, neighborhood, opening and closing dates, officer slots, flat types, 
     * and visibility status.
     */
    public void viewOwnProjects() {
        List<Project> projects = managerController.viewOwnProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
        } else {
            System.out.println("Your Projects:");
            for (Project project : projects) {
                System.out.println("-----------------------------");
                System.out.println("Project Name: " + project.getName());
                System.out.println("Neighborhood: " + project.getNeighborhood());
                System.out.println("Opening Date: " + project.getOpeningDate());
                System.out.println("Closing Date: " + project.getClosingDate());
                System.out.println("Officer Slots: " + project.getOfficerSlots());
                for (Map.Entry<String, Project.FlatTypeDetails> entry : project.getFlatTypes().entrySet()) {
                    String flatType = entry.getKey();
                    Project.FlatTypeDetails details = entry.getValue();
                    System.out.println("Flat Type: " + flatType);
                    System.out.println("Available Units: " + details.getAvailableUnits());
                    System.out.println("Selling Price: $" + details.getSellingPrice());
                }
                System.out.println("Visibility: " + (project.isVisible() ? "Visible" : "Hidden"));
            }
        }
    }

    /**
     * Displays all officer registration requests.
     * If no requests are available, a message is displayed.
     */
    public void viewRequestedOfficer() {
        List<String> requests = managerController.viewRequestedOfficer();
        if (requests.isEmpty()) {
            System.out.println("No officer requests available.");
        } else {
            System.out.println("Officer Requests:");
            System.out.println("-----------------");
            for (String request : requests) {
                System.out.println(request);
            }
        }
    }

    /**
     * Approves an officer's registration for a project.
     * Prompts the user to enter the project name and the officer's name.
     *
     */
    public void approveRegistration() {
        scanner.nextLine(); 
        System.out.print("Please enter the project name: ");
        String projectName = scanner.nextLine().trim();
    
        System.out.print("Please enter the officer name to approve: ");
        String officerName = scanner.nextLine().trim();
    
        try {
            managerController.approveRegistration(projectName, officerName);
            System.out.println("Officer registration approved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Rejects an officer's registration for a project.
     * Prompts the user to enter the project name and the officer's name.
     *
     */
    public void rejectRegistration() {
        scanner.nextLine(); 
        System.out.print("Please enter the project name: ");
        String projectName = scanner.nextLine().trim();
    
        System.out.print("Please enter the officer name to reject: ");
        String officerName = scanner.nextLine().trim();
    
        try {
            managerController.rejectRegistration(projectName, officerName);
            System.out.println("Officer registration rejected successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Approves an application for a project.
     * Displays all pending applications and prompts the user to select one for approval.
     *
     */
    public void approveApplication() {
        scanner.nextLine(); 

        List<Application> applications = managerController.viewAllApplications().stream()
            .filter(app -> !app.getStatus().toString().equals("SUCCESS"))
            .toList();
        if(applications.isEmpty()) {
            System.out.println("No applications available for approval.");
            return;
        }
        applications.forEach(app -> System.out.println("Application ID: " + app.getId() + ", Project Name: " + app.getProjectName() + ", Applicant NRIC: " + app.getApplicantNric() + ", Status: " + app.getStatus()));
        System.out.print("Please enter the application ID to approve: ");
        String applicationId = scanner.nextLine().trim();
    
        try {
            managerController.approveApplication(applicationId);
            System.out.println("Application approved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Rejects an application for a project.
     * Displays all pending applications and prompts the user to select one for rejection.
     *
     */
    public void rejectApplication() {
        scanner.nextLine(); 

        List<Application> applications = managerController.viewAllApplications().stream()
            .filter(app -> !app.getStatus().toString().equals("UNSUCCESSFUL"))
            .toList();
        if(applications.isEmpty()) {
            System.out.println("No applications available for approval.");
            return;
        }
        applications.forEach(app -> System.out.println("Application ID: " + app.getId() + ", Project Name: " + app.getProjectName() + ", Applicant NRIC: " + app.getApplicantNric() + ", Status: " + app.getStatus()));
        System.out.print("Please enter the application ID to reject: ");
        String applicationId = scanner.nextLine().trim();
    
        try {
            managerController.rejectApplication(applicationId);
            System.out.println("Application rejected successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Approves a withdrawal request for an application.
     * Displays all withdrawal requests and prompts the user to select one for approval.
     *
     */
    public void approveWithdrawal() {
        scanner.nextLine(); 

        List<Application> withdrawalApplications = managerController.viewAllApplications().stream()
            .filter(Application::getWithdrawalRequested)
            .toList();

        if (withdrawalApplications.isEmpty()) {
            System.out.println("No withdrawal applications available for approval.");
            return;
        }

        System.out.println("Withdrawal Requests:");
        withdrawalApplications.forEach(app -> System.out.println(
            "Application ID: " + app.getId() +
            ", Project Name: " + app.getProjectName() +
            ", Applicant NRIC: " + app.getApplicantNric() +
            ", Status: " + app.getStatus() +
            "---------------------------------------------"
        ));
        System.out.print("Please enter the application ID to approve withdrawal: ");
        String applicationId = scanner.nextLine().trim();
    
        try {
            managerController.approveWithdrawal(applicationId);
            System.out.println("Withdrawal approved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Rejects a withdrawal request for an application.
     * Displays all withdrawal requests and prompts the user to select one for rejection.
     *
     */
    public void rejectWithdrawal() {
        scanner.nextLine(); 

        List<Application> withdrawalApplications = managerController.viewAllApplications().stream()
        .filter(Application::getWithdrawalRequested)
        .toList();

        if (withdrawalApplications.isEmpty()) {
            System.out.println("No withdrawal applications available for approval.");
            return;
        }

        System.out.println("Withdrawal Requests:");
        withdrawalApplications.forEach(app -> System.out.println(
            "Application ID: " + app.getId() +
            ", Project Name: " + app.getProjectName() +
            ", Applicant NRIC: " + app.getApplicantNric() +
            ", Status: " + app.getStatus()));

        System.out.print("Please enter the application ID to reject withdrawal: ");
        String applicationId = scanner.nextLine().trim();
    
        try {
            managerController.rejectWithdrawal(applicationId);
            System.out.println("Withdrawal rejected successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all enquiries in the system.
     * If no enquiries are available, a message is displayed.
     */
    public void viewAllEnquiries() {
        List<Enquiry> enquiries = managerController.viewAllEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
        } else {
            System.out.println("Enquiries:");
            for (Enquiry enquiry : enquiries) {
                System.out.println("-----------------------------");
                System.out.println("Enquiry ID: " + enquiry.getId());
                System.out.println("Applicant NRIC: " + enquiry.getApplicantNric());
                System.out.println("Project Name: " + enquiry.getProjectName());
                System.out.println("Message: " + enquiry.getDetails());
            }
        }
    }

    /**
     * Allows the manager to reply to a specific enquiry.
     * Prompts the user to enter the enquiry ID and the reply message.
     * 
     */
    public void replyEnquiry() {
        scanner.nextLine(); 
        System.out.print("Please enter the enquiry ID to reply: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.print("Please enter your reply: ");
        String reply = scanner.nextLine().trim();
    
        try {
            managerController.replyEnquiry(enquiryId, reply);
            System.out.println("Enquiry replied successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generates a filtered report of booked applications.
     * The report can be filtered by marital status, flat type, and project name.
     * If no applications match the filter criteria, a message is displayed.
     */
    public void generateReport() {
        List<Application> applications = managerController.generateReport();
    
        System.out.print("Enter marital status to filter by (or press Enter to skip): ");
        scanner.nextLine(); 
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

    /**
     * Toggles the visibility of a specific project.
     * Prompts the user to enter the project name and updates its visibility status.
     * 
     */
    public void toggleProjectVisibility() {
        scanner.nextLine(); 
        System.out.print("Please enter the project name to toggle visibility: ");
        String projectName = scanner.nextLine().trim();
    
        try {
            managerController.toggleProjectVisibility(projectName);
            System.out.println("Project visibility toggled successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
