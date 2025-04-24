package bto.Data;

import bto.Model.Application;
import bto.Model.ApplicationStatus;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ApplicationDao} interface for managing applications using a CSV file as the data source.
 */
public class ApplicationCSVDao implements ApplicationDao {
    private static final String FILEPATH = "./bto/Data/CSV/Applications.csv";
    private static final String HEADER = "ApplicationID,ProjectName,ApplicantNRIC,FlatType,Status,CreatedTime,WithdrawalStatus;";
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Saves a new application to the CSV file.
     *
     * @param application The application to save.
     */
    @Override
    public void save(Application application) {
        if (application.getId() == null || application.getId().isEmpty()) {
            application.setId(generateNewApplicationId());
        }
        
        List<Application> allApps = getAllApplications();
        allApps.add(application);
        writeAllApplications(allApps);
    }

    /**
     * Generates a new unique application ID.
     *
     * @return A new application ID as a string.
     */
    private String generateNewApplicationId() {
        List<Application> apps = getAllApplications();
        int maxId = apps.stream()
            .map(Application::getId)
            .filter(id -> id.matches("\\d+")) 
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
    
        return String.valueOf(maxId + 1);
    }

    /**
     * Updates an existing application in the CSV file.
     *
     * @param application The application to update.
     */
    @Override
    public void update(Application application) {
        List<Application> allApps = getAllApplications().stream()
            .map(app -> app.getId().equals(application.getId()) ? application : app)
            .collect(Collectors.toList());
        writeAllApplications(allApps);
    }

    /**
     * Deletes an application from the CSV file by its ID.
     *
     * @param applicationId The ID of the application to delete.
     */
    @Override
    public void delete(String applicationId) {
        List<Application> filtered = getAllApplications().stream()
            .filter(app -> !app.getId().equals(applicationId))
            .collect(Collectors.toList());
        writeAllApplications(filtered);
    }

    /**
     * Retrieves an application by its ID.
     *
     * @param applicationId The ID of the application.
     * @return An {@code Optional} containing the application if found, or empty if not found.
     */
    @Override
    public Optional<Application> getApplicationById(String applicationId) {
        return getAllApplications().stream()
            .filter(app -> app.getId().equals(applicationId))
            .findFirst();
    }

    /**
     * Retrieves the active application for a specific applicant by their NRIC.
     *
     * @param applicantNric The NRIC of the applicant.
     * @return An {@code Optional} containing the active application if found, or empty if not found.
     */
    @Override
    public Optional<Application> getActiveApplication(String applicantNric) {
        return getAllApplications().stream()
            .filter(app -> app.getApplicantNric().equals(applicantNric))
            .filter(app -> !app.getStatus().isTerminal())
            .findFirst();
    }

    /**
     * Retrieves all applications with a specific status.
     *
     * @param status The status to filter applications by.
     * @return A list of applications with the specified status.
     */
    @Override
    public List<Application> getApplicationsByStatus(String status) {
        return getAllApplications().stream()
            .filter(app -> app.getStatus().name().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all applications for a specific project.
     *
     * @param projectName The name of the project.
     * @return A list of applications for the specified project.
     */
    @Override
    public List<Application> getApplicationsByProject(String projectName) {
        return getAllApplications().stream()
            .filter(app -> app.getProjectName().equalsIgnoreCase(projectName))
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all applications from the CSV file.
     *
     * @return A list of all applications.
     */
    @Override
    public List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        File file = new File(FILEPATH);
        
        if (!file.exists()) initializeCsvFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                Application app = parseApplication(line);
                if (app != null) applications.add(app);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return applications;
    }

    /**
     * Writes all applications to the CSV file.
     *
     * @param applications The list of applications to write.
     */
    private void writeAllApplications(List<Application> applications) {
        try (FileWriter writer = new FileWriter(FILEPATH)) {
            writer.write(HEADER + "\n");
            for (Application app : applications) {
                writer.write(toCsvLine(app) + "\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Parses a CSV line into an {@link Application} object.
     *
     * @param csvLine The CSV line to parse.
     * @return The parsed {@link Application} object, or {@code null} if parsing fails.
     */
    private Application parseApplication(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            Application app = new Application(
                parts[1],  // ProjectName
                parts[2],  // ApplicantNRIC
                parts[3]   // FlatType
            ).setId(parts[0])  // ApplicationID
             .setStatus(ApplicationStatus.valueOf(parts[4].toUpperCase()))
             .setCreatedTime(parts.length > 5 && !parts[5].isEmpty() ? LocalDateTime.parse(parts[5], DT_FORMATTER) : null);
            if (parts.length > 6) {
                app.setWithdrawalRequested(Boolean.parseBoolean(parts[6]));
            }
            return app;
        } catch (Exception e) {
            System.err.println("Error parsing application: " + csvLine);
            return null;
        }
    }

    /**
     * Converts an {@link Application} object to a CSV line.
     *
     * @param app The application to convert.
     * @return A string representing the application in CSV format.
     */
    private String toCsvLine(Application app) {
        return String.join(",",
            app.getId(),
            app.getProjectName(),
            app.getApplicantNric(),
            app.getFlatType(),
            app.getStatus().name(),
            app.getCreatedTime().format(DT_FORMATTER),
            String.valueOf(app.getWithdrawalRequested())
        );
    }

    /**
     * Initializes the CSV file by creating it and writing the header.
     */
    private void initializeCsvFile() {
        try (FileWriter writer = new FileWriter(FILEPATH)) {
            writer.write(HEADER + "\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}