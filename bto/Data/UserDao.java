package bto.Data;

import bto.Model.User;

/**
 * Data Access Object (DAO) interface for managing user-related operations.
 */
public interface UserDao {

    /**
     * Reads user data from a specified CSV file and loads it into the system.
     *
     * @param filePath The path to the CSV file containing user data.
     */
    void readUsers(String filePath);

    /**
     * Updates an existing user's data in the data source.
     *
     * @param user The user whose data is to be updated.
     */
    void updateUser(User user);
}
