package bto.Data;

import bto.Model.Enquiry;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EnquiryCSVDao implements EnquiryDao {
    private static final String CSV_FILE = "./bto/Data/CSV/Enquiries.csv";
    private static final String HEADER = "EnquiryID,ApplicantNRIC,ProjectName,EnquiryDetails,Reply";

    @Override
    public void save(Enquiry enquiry) {
        List<Enquiry> allEnquiries = readAllEnquiries();
        
        // Generate ID if new enquiry
        if (enquiry.getId() == 0) {
            int newId = generateNewId(allEnquiries);
            enquiry = new Enquiry(newId, enquiry.getApplicantNric(), 
                                enquiry.getProjectName(), enquiry.getDetails(), 
                                enquiry.getReply());
        }
        
        allEnquiries.add(enquiry);
        writeAllEnquiries(allEnquiries);
    }

    @Override
    public void update(Enquiry enquiry) {
        List<Enquiry> updated = readAllEnquiries().stream()
            .map(e -> e.getId() == enquiry.getId() ? enquiry : e)
            .collect(Collectors.toList());
        writeAllEnquiries(updated);
    }

    @Override
    public void delete(int enquiryId) {
        List<Enquiry> filtered = readAllEnquiries().stream()
            .filter(e -> e.getId() != enquiryId)
            .collect(Collectors.toList());
        writeAllEnquiries(filtered);
    }

    @Override
    public Enquiry findById(int enquiryId) {
        return readAllEnquiries().stream()
            .filter(e -> e.getId() == enquiryId)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Enquiry> getEnquiriesByApplicant(String applicantNric) {
        return readAllEnquiries().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicantNric))
            .collect(Collectors.toList());
    }

    @Override
    public List<Enquiry> getEnquiriesByProject(String projectName) {
        return readAllEnquiries().stream()
            .filter(e -> e.getProjectName().equalsIgnoreCase(projectName))
            .collect(Collectors.toList());
    }

    @Override
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
                Integer.parseInt(parts[0]),   // ID
                parts[1],                     // ApplicantNRIC
                parts[2],                     // ProjectName
                parts[3],                     // EnquiryDetails
                parts[4].equals("null") ? null : parts[4]  // Reply
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
            escapeCommas(enq.getDetails()),
            enq.getReply() != null ? escapeCommas(enq.getReply()) : "null"
        );
    }

    private String escapeCommas(String input) {
        return input.contains(",") ? "\"" + input + "\"" : input;
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