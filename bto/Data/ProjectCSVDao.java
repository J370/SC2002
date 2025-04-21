package bto.Data;

import bto.Model.Project;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectCSVDao implements ProjectDao {
    private static final String FILEPATH = "./bto/Data/CSV/ProjectList.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final String HEADER = "Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Assigned Officer,Requested Officer,Rejected Officer,Visible";


    @Override
    public void saveProject(Project project) {
        List<Project> allProjects = getAllProjects();
        allProjects.add(project);
        writeAllProjects(allProjects);
    }
    
    @Override
    public void updateProject(Project project) {
        List<Project> allProjects = getAllProjects().stream()
                .map(p -> p.getName().equals(project.getName()) ? project : p)
                .collect(Collectors.toList());
        writeAllProjects(allProjects);
    }
    
    @Override
    public void deleteProject(String projectId) {
        List<Project> allProjects = getAllProjects().stream()
                .filter(p -> !p.getName().equals(projectId))
                .collect(Collectors.toList());
        writeAllProjects(allProjects);
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Project p = parseProject(line);
                if (p != null) projects.add(p);
            }
        } catch (IOException e) {
            System.err.println("Error reading projects: " + e.getMessage());
        }
        return projects;
    }

    @Override
    public Project getProjectById(String projectId) {
        return getAllProjects().stream()
                .filter(p -> p.getName().equalsIgnoreCase(projectId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Project with ID " + projectId + " not found"));
    }

    @Override
    public void decreaseAvailableUnits(String projectId, String flatType, int count) {
        List<Project> allProjects = getAllProjects();
        allProjects.forEach(p -> {
            if (p.getName().equals(projectId)) {
                Project.FlatTypeDetails details = p.getFlatTypes().get(flatType);
                if (details != null) {
                    details.setAvailableUnits(details.getAvailableUnits() - count);
                }
            }
        });
        writeAllProjects(allProjects);
    }

    private void initializeCsvFile() {
        try (FileWriter writer = new FileWriter(FILEPATH)) {
            writer.write(HEADER + "\n");
        } catch (IOException e) {
            System.err.println("Error creating CSV file: " + e.getMessage());
        }
    }

    private Project parseProject(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            Map<String, Project.FlatTypeDetails> flatTypes = new HashMap<>();

            List<String> assignedOfficers = (parts[12].equals("null") || parts[12].isEmpty())
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(parts[12].split("\\|")));
            List<String> requestedOfficers = (parts[13].equals("null") || parts[13].isEmpty())
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(parts[13].split("\\|")));
            List<String> rejectedOfficers = (parts[14].equals("null") || parts[14].isEmpty())
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(parts[14].split("\\|")));

                        // Parse Type 1
            if (!parts[2].isEmpty()) {
                String type1 = parts[2];
                int units1 = parts[3].isEmpty() ? 0 : Integer.parseInt(parts[3]);
                double price1 = parts[4].isEmpty() ? 0 : Double.parseDouble(parts[4]);
                flatTypes.put(type1, new Project.FlatTypeDetails(units1, price1));
            }
            // Parse Type 2
            if (!parts[5].isEmpty()) {
                String type2 = parts[5];
                int units2 = parts[6].isEmpty() ? 0 : Integer.parseInt(parts[6]);
                double price2 = parts[7].isEmpty() ? 0 : Double.parseDouble(parts[7]);
                flatTypes.put(type2, new Project.FlatTypeDetails(units2, price2));
            }

            // Remove accidental "null" string if present
            assignedOfficers.removeIf(s -> s.equals("null"));
            requestedOfficers.removeIf(s -> s.equals("null"));
            rejectedOfficers.removeIf(s -> s.equals("null"));
    
            boolean isVisible = parts.length > 15 && parts[15].trim().equalsIgnoreCase("true");
    
            Project project = new Project(
                parts[0],  // name
                parts[1],  // neighborhood
                flatTypes,
                LocalDate.parse(parts[8].trim(), DATE_FORMATTER),
                LocalDate.parse(parts[9].trim(), DATE_FORMATTER),
                parts[10],
                Integer.parseInt(parts[11]),
                assignedOfficers,
                requestedOfficers,
                rejectedOfficers,
                isVisible
            );
    
    
            return project;
        } catch (Exception e) {
            System.err.println("Error parsing project: " + csvLine);
            return null;
        }
    }


    private void writeAllProjects(List<Project> projects) {
        try (FileWriter writer = new FileWriter(FILEPATH)) {
            writer.write(HEADER + "\n");
            for (Project p : projects) {
                writer.write(toCsvLine(p) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving projects: " + e.getMessage());
        }
    }

    private String toCsvLine(Project p) {
        Map<String, Project.FlatTypeDetails> flatTypes = p.getFlatTypes();
        List<String> types = new ArrayList<>(flatTypes.keySet());

    
        return String.join(",",
            p.getName(),
            p.getNeighborhood(),
            types.size() > 0 ? types.get(0) : "",  // Type 1
            types.size() > 0 ? String.valueOf(flatTypes.get(types.get(0)).getAvailableUnits()) : "",
            types.size() > 0 ? String.format("%.0f", flatTypes.get(types.get(0)).getSellingPrice()) : "",
            types.size() > 1 ? types.get(1) : "",  // Type 2
            types.size() > 1 ? String.valueOf(flatTypes.get(types.get(1)).getAvailableUnits()) : "",
            types.size() > 1 ? String.format("%.0f", flatTypes.get(types.get(1)).getSellingPrice()) : "",
            p.getOpeningDate().format(DATE_FORMATTER),
            p.getClosingDate().format(DATE_FORMATTER),
            p.getManager(),
            String.valueOf(p.getOfficerSlots()),
            p.getAssignedOfficers().isEmpty() ? "null" : String.join("|", p.getAssignedOfficers()),
            p.getRequestedOfficers().isEmpty() ? "null" : String.join("|", p.getRequestedOfficers()),
            p.getRejectedOfficers().isEmpty() ? "null" : String.join("|", p.getRejectedOfficers()),
            String.valueOf(p.isVisible())
        );
    }


}
