package bto.Model;

/**
 * Represents an officer in the system.
 * An officer is a type of user responsible for handling project-related tasks and enquiries.
 */
public class Officer extends User {

    /**
     * Constructs an Officer with the specified details.
     *
     * @param name The name of the officer.
     * @param nric The NRIC of the officer.
     * @param age The age of the officer.
     * @param maritalStatus The marital status of the officer.
     * @param password The password for the officer's account.
     */
    public Officer(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }
}