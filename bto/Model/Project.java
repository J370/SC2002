package bto.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a housing project in the system.
 * A project contains details such as flat types, application dates, and officer assignments.
 */
public class Project {
    private String projectName;
    private String neighborhood;
    private Map<String, FlatTypeDetails> flatTypes;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String manager;
    private int officerSlots;
    private List<String> assignedOfficers;
    private List<String> requestedOfficers;
    private List<String> rejectedOfficers;
    private boolean isVisible;

    /**
     * Represents the details of a flat type in the project.
     */
    public static class FlatTypeDetails {
        private int availableUnits;
        private double sellingPrice;

        /**
         * Constructs a FlatTypeDetails object with the specified available units and selling price.
         *
         * @param availableUnits The number of available units for this flat type.
         * @param sellingPrice The selling price of this flat type.
         */
        public FlatTypeDetails(int availableUnits, double sellingPrice) {
            this.availableUnits = availableUnits;
            this.sellingPrice = sellingPrice;
        }

        /**
         * Gets the number of available units for this flat type.
         *
         * @return The number of available units.
         */
        public int getAvailableUnits() { return availableUnits; }

        /**
         * Sets the number of available units for this flat type.
         *
         * @param units The new number of available units.
         */
        public void setAvailableUnits(int units) { this.availableUnits = units; }

        /**
         * Gets the selling price of this flat type.
         *
         * @return The selling price.
         */
        public double getSellingPrice() { return sellingPrice; }

        /**
         * Sets the selling price of this flat type.
         *
         * @param price The new selling price.
         */
        public void setSellingPrice(double price) { this.sellingPrice = price; }
    }

    /**
     * Constructs a Project with the specified details.
     *
     * @param projectName The name of the project.
     * @param neighborhood The neighborhood where the project is located.
     * @param flatTypes A map of flat types and their details.
     * @param openingDate The opening date for applications.
     * @param closingDate The closing date for applications.
     * @param manager The name of the manager responsible for the project.
     * @param officerSlots The number of officer slots available for the project.
     * @param assignedOfficers A list of officers assigned to the project.
     * @param requestedOfficers A list of officers who have requested to join the project.
     * @param rejectedOfficers A list of officers who have been rejected from the project.
     * @param isVisible Whether the project is visible to applicants.
     */
    public Project(String projectName, String neighborhood, Map<String, FlatTypeDetails> flatTypes,
                   LocalDate openingDate, LocalDate closingDate, String manager, int officerSlots,
                   List<String> assignedOfficers, List<String> requestedOfficers, List<String> rejectedOfficers,
                   boolean isVisible) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.flatTypes = new HashMap<>(flatTypes);
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.manager = manager;
        this.officerSlots = Math.min(officerSlots, 10); // Limit to 10 officers
        this.assignedOfficers = assignedOfficers;
        this.requestedOfficers = requestedOfficers;
        this.rejectedOfficers = rejectedOfficers;
        this.isVisible = isVisible;
    }

    /**
     * Checks if an applicant is eligible to apply for the project.
     *
     * @param applicant The applicant to check eligibility for.
     * @return {@code true} if the applicant is eligible, {@code false} otherwise.
     */
    public boolean isEligible(Applicant applicant) {
        if (applicant.getMaritalStatus().equals("Single")) {
            return applicant.getAge() >= 35 && flatTypes.containsKey("2-Room");
        }
        return applicant.getAge() >= 21;
    }

    /**
     * Retrieves a list of flat types with available units.
     *
     * @return A list of flat types with available units.
     */
    public List<String> getAvailableFlatTypes() {
        List<String> available = new ArrayList<>();
        for (Map.Entry<String, FlatTypeDetails> entry : flatTypes.entrySet()) {
            if (entry.getValue().getAvailableUnits() > 0) {
                available.add(entry.getKey());
            }
        }
        return available;
    }

    /**
     * Checks if a specific flat type has available units.
     *
     * @param flatType The flat type to check.
     * @return {@code true} if the flat type has available units, {@code false} otherwise.
     */
    public boolean hasAvailableUnits(String flatType) {
        FlatTypeDetails details = flatTypes.get(flatType);
        return details != null && details.getAvailableUnits() > 0;
    }

    /**
     * Toggles the visibility of the project.
     */
    public void toggleVisibility() { this.isVisible = !this.isVisible; }

    /**
     * Checks if the project is visible.
     *
     * @return {@code true} if the project is visible, {@code false} otherwise.
     */
    public boolean isVisible() { return this.isVisible; }

    /**
     * Checks if the application period for the project is open.
     *
     * @return {@code true} if the application period is open, {@code false} otherwise.
     */
    public boolean isApplicationOpen() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(openingDate) && !today.isAfter(closingDate);
    }

    /**
     * Decreases the available units for a specific flat type.
     *
     * @param flatType The flat type to decrease units for.
     * @param count The number of units to decrease.
     */
    public void decreaseAvailableUnits(String flatType, int count) {
        FlatTypeDetails details = flatTypes.get(flatType);
        if (details != null) {
            details.setAvailableUnits(details.getAvailableUnits() - count);
        }
    }

    /**
     * Gets the name of the project.
     *
     * @return The name of the project.
     */
    public String getName() { return projectName; }

    /**
     * Gets the neighborhood where the project is located.
     *
     * @return The neighborhood of the project.
     */
    public String getNeighborhood() { return neighborhood; }

    /**
     * Gets the flat types and their details for the project.
     *
     * @return A map of flat types and their details.
     */
    public Map<String, FlatTypeDetails> getFlatTypes() { return new HashMap<>(flatTypes); }

    /**
     * Gets the opening date for applications to the project.
     *
     * @return The opening date for applications.
     */
    public LocalDate getOpeningDate() { return openingDate; }

    /**
     * Gets the closing date for applications to the project.
     *
     * @return The closing date for applications.
     */
    public LocalDate getClosingDate() { return closingDate; }

    /**
     * Gets the name of the manager responsible for the project.
     *
     * @return The name of the manager.
     */
    public String getManager() { return manager; }

    /**
     * Gets the number of officer slots available for the project.
     *
     * @return The number of officer slots.
     */
    public int getOfficerSlots() { return officerSlots; }

    /**
     * Gets the list of officers assigned to the project.
     *
     * @return A list of assigned officers.
     */
    public List<String> getAssignedOfficers() { return new ArrayList<>(assignedOfficers); }

    /**
     * Gets the list of officers who have requested to join the project.
     *
     * @return A list of requested officers.
     */
    public List<String> getRequestedOfficers() { return new ArrayList<>(requestedOfficers); }

    /**
     * Gets the list of officers who have been rejected from the project.
     *
     * @return A list of rejected officers.
     */
    public List<String> getRejectedOfficers() { return new ArrayList<>(rejectedOfficers); }

    /**
     * Checks if the project is visible.
     *
     * @return {@code true} if the project is visible, {@code false} otherwise.
     */
    public boolean getVisible() { return isVisible; }

    /**
     * Sets the flat types and their details for the project.
     *
     * @param flatTypes A map of flat types and their details.
     */
    public void setFlatTypes(Map<String, FlatTypeDetails> flatTypes) { this.flatTypes = new HashMap<>(flatTypes); }

    /**
     * Sets the number of officer slots available for the project.
     * The number of slots is capped at a maximum of 10.
     *
     * @param slots The number of officer slots to set.
     */
    public void setOfficerSlots(int slots) { this.officerSlots = Math.min(slots, 10); }

    /**
     * Sets the visibility of the project.
     *
     * @param visible {@code true} to make the project visible, {@code false} otherwise.
     */
    public void setVisible(boolean visible) { this.isVisible = visible; }

    /**
     * Adds an officer to the list of assigned officers for the project.
     *
     * @param officerName The name of the officer to assign.
     */
    public void addAssignedOfficer(String officerName) { assignedOfficers.add(officerName); }

    /**
     * Adds an officer to the list of requested officers for the project.
     *
     * @param officerName The name of the officer to add to the requested list.
     */
    public void addRequestedOfficer(String officerName) { requestedOfficers.add(officerName); }

    /**
     * Adds an officer to the list of rejected officers for the project.
     *
     * @param officerName The name of the officer to reject.
     */
    public void addRejectedOfficer(String officerName) { rejectedOfficers.add(officerName); }

    /**
     * Removes an officer from the list of assigned officers for the project.
     *
     * @param officerName The name of the officer to remove from the assigned list.
     */
    public void removeAssignedOfficer(String officerName) { assignedOfficers.remove(officerName); }

    /**
     * Removes an officer from the list of requested officers for the project.
     *
     * @param officerName The name of the officer to remove from the requested list.
     */
    public void removeRequestedOfficer(String officerName) { requestedOfficers.remove(officerName); }

    /**
     * Removes an officer from the list of rejected officers for the project.
     *
     * @param officerName The name of the officer to remove from the rejected list.
     */
    public void removeRejectedOfficer(String officerName) { rejectedOfficers.remove(officerName); }
}