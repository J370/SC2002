package bto.Model;

import java.util.HashMap;

/**
 * Represents a user in the system.
 * This is an abstract class that serves as the base for specific user types such as Applicant, Manager, and Officer.
 */
public abstract class User {
    private static HashMap<String, User> users = new HashMap<>();
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;

    /**
     * Constructs a User with the specified details.
     *
     * @param name The name of the user.
     * @param nric The NRIC of the user.
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user.
     * @param password The password for the user's account.
     */
    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    /**
     * Adds a user to the system.
     *
     * @param id The unique identifier for the user.
     * @param user The user to add.
     */
    public static void addUser(String id, User user) {
        users.put(id, user);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return The user associated with the given ID, or {@code null} if no user is found.
     */
    public static User getUser(String id) {
        return users.get(id);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A {@link HashMap} containing all users, where the key is the user's unique identifier.
     */
    public static HashMap<String, User> getAllUsers() {
        return users;
    }

    /**
     * Gets the name of the user.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the NRIC of the user.
     *
     * @return The user's NRIC.
     */
    public String getNric() {
        return nric;
    }

    /**
     * Gets the age of the user.
     *
     * @return The user's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the marital status of the user.
     *
     * @return The user's marital status.
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Gets the password of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The new name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the NRIC of the user.
     *
     * @param nric The new NRIC of the user.
     */
    public void setNric(String nric) {
        this.nric = nric;
    }

    /**
     * Sets the age of the user.
     *
     * @param age The new age of the user.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the marital status of the user.
     *
     * @param maritalStatus The new marital status of the user.
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A string containing the user's details in CSV format.
     */
    @Override
    public String toString() {
        return name + ',' + nric + "," + age + "," + maritalStatus + "," + password;
    }
}
