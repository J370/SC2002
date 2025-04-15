package bto.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
    private String projectName;
    private String neighborhood;
    private Map<String, FlatTypeDetails> flatTypes;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String manager;
    private int officerSlots;
    private List<String> assignedOfficers;
    private boolean isVisible;

    public static class FlatTypeDetails {
        private int availableUnits;
        private double sellingPrice;

        public FlatTypeDetails(int availableUnits, double sellingPrice) {
            this.availableUnits = availableUnits;
            this.sellingPrice = sellingPrice;
        }

        public int getAvailableUnits() { return availableUnits; }
        public void setAvailableUnits(int units) { this.availableUnits = units; }
        
        public double getSellingPrice() { return sellingPrice; }
        public void setSellingPrice(double price) { this.sellingPrice = price; }
    }

    public Project (String projectName, String neighborhood, Map<String, FlatTypeDetails> flatTypes,
                    LocalDate openingDate, LocalDate closingDate, String manager, int officerSlots) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.flatTypes = new HashMap<>(flatTypes);
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.manager = manager;
        this.officerSlots = Math.min(officerSlots, 10); // Limit to 10 officers
        this.assignedOfficers = new ArrayList<>();
        this.isVisible = true;
    }


    public boolean isEligible(Applicant applicant) {
        if (applicant.getMaritalStatus().equals("Single")) {
            return applicant.getAge() >= 35 && flatTypes.containsKey("2-Room");
        }
        return applicant.getAge() >= 21;
    }

    public List<String> getAvailableFlatTypes() {
        List<String> available = new ArrayList<>();
        for (Map.Entry<String, FlatTypeDetails> entry : flatTypes.entrySet()) {
            if (entry.getValue().getAvailableUnits() > 0) {
                available.add(entry.getKey());
            }
        }
        return available;
    }

    public boolean hasAvailableUnits(String flatType) {
        FlatTypeDetails details = flatTypes.get(flatType);
        return details != null && details.getAvailableUnits() > 0;
    }

    public void toggleVisibility() {this.isVisible = !this.isVisible;}
    public boolean isVisible() {return this.isVisible;}
    public boolean isApplicationOpen() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(openingDate) && !today.isAfter(closingDate);
    }

    public void decreaseAvailableUnits(String flatType, int count) {}

    public String getName() {return projectName;}
    public String getNeighborhood() {return neighborhood;}
    public Map<String, FlatTypeDetails> getFlatTypes() { return new HashMap<>(flatTypes);}
    public LocalDate getOpeningDate() {return openingDate;}
    public LocalDate getClosingDate() {return closingDate;}
    public String getManager() {return manager;}
    public int getOfficerSlots() {return officerSlots;}
    public List<String> getAssignedOfficers() {return new ArrayList<>(assignedOfficers);}

    // === Setters ===
    public void setFlatTypes(Map<String, FlatTypeDetails> flatTypes) {this.flatTypes = new HashMap<>(flatTypes);}
    public void setOfficerSlots(int slots) {this.officerSlots = Math.min(slots, 10);}

    public void addOfficer(String officerId) {
        if (assignedOfficers.size() < officerSlots) {
            assignedOfficers.add(officerId);
        }
    }

}