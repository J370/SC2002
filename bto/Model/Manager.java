package bto.Model;

/**
 * Represents a manager in the system.
 * A manager is a type of user responsible for managing housing projects.
 */
public class Manager extends User {

    /**
     * Constructs a Manager with the specified details.
     *
     * @param name The name of the manager.
     * @param nric The NRIC of the manager.
     * @param age The age of the manager.
     * @param maritalStatus The marital status of the manager.
     * @param password The password for the manager's account.
     */
    public Manager(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }
}