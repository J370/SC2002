package bto.Data;

import bto.Model.Enquiry;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementation of the {@link EnquiryDao} interface for managing enquiries using a CSV file as the data source.
 */
public class EnquiryCSVDao implements EnquiryDao {
    private static final String CSV_FILE = "./bto/Data/CSV/Enquiries.csv";
    private static final String HEADER = "EnquiryID,ApplicantNRIC,ProjectName,EnquiryDetails,CreatedTime,Reply,RepliedBy,RepliedTime";
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Saves a new enquiry to the CSV file.
     *
     * @param enquiry The enquiry to save.
     */
    @Override
    public void save(Enquiry enquiry) {
        List<Enquiry> allEnquiries = readAllEnquiries();
        
        if (enquiry.getId() == 0) {
            int newId = generateNewId(allEnquiries);
            enquiry = new Enquiry(newId, enquiry.getApplicantNric(), 
                                enquiry.getProjectName(), enquiry.getDetails(), LocalDateTime.now(), 
                                enquiry.getReply(), enquiry.getRepliedBy(), enquiry.getRepliedTime());
        }
        
        allEnquiries.add(enquiry);
        writeAllEnquiries(allEnquiries);
    }

    /**
     * Updates an existing enquiry in the CSV file.
     *
     * @param enquiry The enquiry to update.
     */
    @Override
    public void update(Enquiry enquiry) {
        List<Enquiry> updated = readAllEnquiries().stream()
            .map(e -> e.getId() == enquiry.getId() ? enquiry : e)
            .collect(Collectors.toList());
        writeAllEnquiries(updated);
    }

    /**
     * Deletes an enquiry from the CSV file by its ID.
     *
     * @param enquiryId The ID of the enquiry to delete.
     */
    @Override
    public void delete(int enquiryId) {
        List<Enquiry> filtered = readAllEnquiries().stream()
            .filter(e -> e.getId() != enquiryId)
            .collect(Collectors.toList());
        writeAllEnquiries(filtered);
    }

    /**
     * Finds an enquiry by its ID.
     *
     * @param enquiryId The ID of the enquiry.
     * @return The enquiry if found, or {@code null} if not found.
     */
    @Override
    public Enquiry findById(int enquiryId) {
        return readAllEnquiries().stream()
            .filter(e -> e.getId() == enquiryId)
            .findFirst()
            .orElse(null);
    }

    /**
     * Retrieves all enquiries submitted by a specific applicant.
     *
     * @param applicantNric The NRIC of the applicant.
     * @return A list of enquiries submitted by the applicant.
     */
    @Override
    public List<Enquiry> getEnquiriesByApplicant(String applicantNric) {
        return readAllEnquiries().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicantNric))
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all enquiries for a specific project.
     *
     * @param projectName The name of the project.
     * @return A list of enquiries for the specified project.
     */
    @Override
    public List<Enquiry> getEnquiriesByProject(String projectName) {
        return readAllEnquiries().stream()
            .filter(e -> e.getProjectName().equalsIgnoreCase(projectName))
            .collect(Collectors.toList());
    }

    /**
     * Retrieves all enquiries from the CSV file.
     *
     * @return A list of all enquiries.
     */
    @Override
    public List<Enquiry> getAllEnquiries() {
        return readAllEnquiries();
    }

    /**
     * Reads all enquiries from the CSV file.
     *
     * @return A list of all enquiries.
     */
    public List<Enquiry> readAllEnquiries() {
        List<Enquiry> enquiries = new ArrayList<>();
        File file = new File(CSV_FILE);
        
        if (!file.exists()) initializeCsvFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                Enquiry enq = parseEnquiry(line);
                if (enq != null) enquiries.add(enq);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return enquiries;
    }

    /**
     * Writes all enquiries to the CSV file.
     *
     * @param enquiries The list of enquiries to write.
     */
    private void writeAllEnquiries(List<Enquiry> enquiries) {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write(HEADER + "\n");
            for (Enquiry enq : enquiries) {
                writer.write(toCsvLine(enq) + "\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Parses a CSV line into an {@link Enquiry} object.
     *
     * @param csvLine The CSV line to parse.
     * @return The parsed {@link Enquiry} object, or {@code null} if parsing fails.
     */
    private Enquiry parseEnquiry(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            return new Enquiry(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                parts[4].equals("null") ? null : LocalDateTime.parse(parts[4], DT_FORMATTER),
                parts[5].equals("null") ? null : parts[5],
                parts[6].equals("null") ? null : parts[6],
                parts[7].equals("null") ? null : LocalDateTime.parse(parts[7], DT_FORMATTER)
            );
        } catch (Exception e) {
            System.err.println("Error parsing enquiry: " + csvLine);
            return null;
        }
    }

    /**
     * Converts an {@link Enquiry} object to a CSV line.
     *
     * @param enq The enquiry to convert.
     * @return A string representing the enquiry in CSV format.
     */
    private String toCsvLine(Enquiry enq) {
        return String.join(",",
            String.valueOf(enq.getId()),
            enq.getApplicantNric(),
            enq.getProjectName(),
            enq.getDetails() != null ? enq.getDetails() : "null",
            enq.getCreatedTime() != null ? enq.getCreatedTime().format(DT_FORMATTER) : "null",
            enq.getReply() != null ? enq.getReply() : "null",
            enq.getRepliedBy() != null ? enq.getRepliedBy() : "null",
            enq.getRepliedTime() != null ? enq.getRepliedTime().format(DT_FORMATTER) : "null"
        );
    }

    /**
     * Generates a new unique ID for an enquiry.
     *
     * @param existing The list of existing enquiries.
     * @return A new unique ID.
     */
    private int generateNewId(List<Enquiry> existing) {
        return existing.stream()
            .mapToInt(Enquiry::getId)
            .max()
            .orElse(0) + 1;
    }

    /**
     * Initializes the CSV file by creating it and writing the header.
     */
    private void initializeCsvFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write(HEADER + "\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}