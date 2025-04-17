package bto.Data;

import bto.Model.Enquiry;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnquiryCSVDao {
    private static final String CSV_FILE = "./bto/Data/CSV/Enquiries.csv";
    private static final String HEADER = "EnquiryID,ApplicantNRIC,ProjectName,EnquiryDetails,CreatedTime,Reply,RepliedBy,RepliedTime";
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void save(Enquiry enquiry) {
        List<Enquiry> allEnquiries = readAllEnquiries();
        
        // Generate ID if new enquiry
        if (enquiry.getId() == 0) {
            int newId = generateNewId(allEnquiries);
            enquiry = new Enquiry(newId, enquiry.getApplicantNric(), 
                                enquiry.getProjectName(), enquiry.getDetails(), LocalDateTime.now(), 
                                enquiry.getReply(), enquiry.getRepliedBy(), enquiry.getRepliedTime());
        }
        
        allEnquiries.add(enquiry);
        writeAllEnquiries(allEnquiries);
    }

    public void update(Enquiry enquiry) {
        List<Enquiry> updated = readAllEnquiries().stream()
            .map(e -> e.getId() == enquiry.getId() ? enquiry : e)
            .collect(Collectors.toList());
        writeAllEnquiries(updated);
    }

    public void delete(int enquiryId) {
        List<Enquiry> filtered = readAllEnquiries().stream()
            .filter(e -> e.getId() != enquiryId)
            .collect(Collectors.toList());
        writeAllEnquiries(filtered);
    }

    public Enquiry findById(int enquiryId) {
        return readAllEnquiries().stream()
            .filter(e -> e.getId() == enquiryId)
            .findFirst()
            .orElse(null);
    }

    public List<Enquiry> getEnquiriesByApplicant(String applicantNric) {
        return readAllEnquiries().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicantNric))
            .collect(Collectors.toList());
    }

    public List<Enquiry> getEnquiriesByProject(String projectName) {
        return readAllEnquiries().stream()
            .filter(e -> e.getProjectName().equalsIgnoreCase(projectName))
            .collect(Collectors.toList());
    }

    public List<Enquiry> getAllEnquiries() {
        return readAllEnquiries();
    }

    private List<Enquiry> readAllEnquiries() {
        List<Enquiry> enquiries = new ArrayList<>();
        File file = new File(CSV_FILE);
        
        if (!file.exists()) initializeCsvFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                Enquiry enq = parseEnquiry(line);
                if (enq != null) enquiries.add(enq);
            }
        } catch (IOException e) {
            System.err.println("Error reading enquiries: " + e.getMessage());
        }
        return enquiries;
    }

    private void writeAllEnquiries(List<Enquiry> enquiries) {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write(HEADER + "\n");
            for (Enquiry enq : enquiries) {
                writer.write(toCsvLine(enq) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving enquiries: " + e.getMessage());
        }
    }

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

    private String escapeCommas(String input) {
        return input.contains(",") ? "\"" + input + "\"" : input;
    }

    private String unescapeCommas(String input) {
        return input.startsWith("\"") && input.endsWith("\"") ? 
            input.substring(1, input.length()-1) : input;
    }

    private int generateNewId(List<Enquiry> existing) {
        return existing.stream()
            .mapToInt(Enquiry::getId)
            .max()
            .orElse(0) + 1;
    }

    private void initializeCsvFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write(HEADER + "\n");
        } catch (IOException e) {
            System.err.println("Error creating CSV file: " + e.getMessage());
        }
    }
}