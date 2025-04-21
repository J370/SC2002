package bto.Data;

import bto.Model.Application;
import bto.Model.ApplicationStatus;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class ApplicationCSVDao implements ApplicationDao {
    private static final String FILEPATH = "./bto/Data/CSV/Applications.csv";
    private static final String HEADER = "ApplicationID,ProjectName,ApplicantNRIC,FlatType,Status,CreatedTime;";
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void save(Application application) {
        // Generate ID if missing
        if (application.getId() == null || application.getId().isEmpty()) {
            application.setId(generateNewApplicationId());
        }
        
        List<Application> allApps = getAllApplications();
        allApps.add(application);
        writeAllApplications(allApps);
    }

    private String generateNewApplicationId() {
        List<Application> apps = getAllApplications();
        int maxId = apps.stream()
            .map(Application::getId)
            .filter(id -> id.startsWith("APP-"))
            .map(id -> id.substring(4))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
        
        return String.format("APP-%03d", maxId + 1);
    }

    @Override
    public void update(Application application) {
        List<Application> allApps = getAllApplications().stream()
            .map(app -> app.getId().equals(application.getId()) ? application : app)
            .collect(Collectors.toList());
        writeAllApplications(allApps);
    }

    @Override
    public void delete(String applicationId) {
        List<Application> filtered = getAllApplications().stream()
            .filter(app -> !app.getId().equals(applicationId))
            .collect(Collectors.toList());
        writeAllApplications(filtered);
    }

    @Override
    public Optional<Application> getApplicationById(String applicationId) {
        return getAllApplications().stream()
            .filter(app -> app.getId().equals(applicationId))
            .findFirst();
    }

    @Override
    public Optional<Application> getActiveApplication(String applicantNric) {
        return getAllApplications().stream()
            .filter(app -> app.getApplicantNric().equals(applicantNric))
            .filter(app -> !app.getStatus().isTerminal())
            .findFirst();
    }

    @Override
    public List<Application> getApplicationsByStatus(String status) {
        return getAllApplications().stream()
            .filter(app -> app.getStatus().name().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    @Override
    public List<Application> getApplicationsByProject(String projectName) {
        return getAllApplications().stream()
            .filter(app -> app.getProjectName().equalsIgnoreCase(projectName))
            .collect(Collectors.toList());
    }

    @Override
    public List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        File file = new File(FILEPATH);
        
        if (!file.exists()) initializeCsvFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                Application app = parseApplication(line);
                if (app != null) applications.add(app);
            }
        } catch (IOException e) {
            System.err.println("Error reading applications: " + e.getMessage());
        }
        return applications;
    }

    private void writeAllApplications(List<Application> applications) {
        try (FileWriter writer = new FileWriter(FILEPATH)) {
            writer.write(HEADER + "\n");
            for (Application app : applications) {
                writer.write(toCsvLine(app) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving applications: " + e.getMessage());
        }
    }


    private Application parseApplication(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            return new Application(
                parts[1],  // ProjectName
                parts[2],  // ApplicantNRIC
                parts[3]   // FlatType
            ).setId(parts[0])  // ApplicationID
             .setStatus(ApplicationStatus.valueOf(parts[4].toUpperCase()))
             .setCreatedTime(parts.length > 5 && !parts[5].isEmpty() ? LocalDateTime.parse(parts[5], DT_FORMATTER) : null);
        } catch (Exception e) {
            System.err.println("Error parsing application: " + csvLine);
            return null;
        }
    }
    
    private String toCsvLine(Application app) {
        return String.join(",",
            app.getId(),
            app.getProjectName(),
            app.getApplicantNric(),
            app.getFlatType(),
            app.getStatus().name(),
            app.getCreatedTime().format(DT_FORMATTER)
        );
    }


    private void initializeCsvFile() {
        try (FileWriter writer = new FileWriter(FILEPATH)) {
            writer.write(HEADER + "\n");
        } catch (IOException e) {
            System.err.println("Error creating CSV file: " + e.getMessage());
        }
    }
}