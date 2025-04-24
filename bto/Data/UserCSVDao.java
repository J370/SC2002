package bto.Data;

import bto.Model.*;
import java.io.*;

/**
 * Implementation of the {@link UserDao} interface for managing user data using CSV files.
 */
public class UserCSVDao implements UserDao {

    /**
     * Reads user data from a specified CSV file and loads it into the system.
     *
     * @param filePath The path to the CSV file containing user data.
     */
    @Override
    public void readUsers(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                }
                else {
                    String[] data = line.split(",");
                    filePath = filePath.replace("./bto/Data/CSV/", "");
                    switch (filePath) {
                        case "ApplicantList.csv":
                            User.addUser(data[1], new Applicant(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]));
                            break;
                        case "ManagerList.csv":
                            User.addUser(data[1], new Manager(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]));
                            break;
                        case "OfficerList.csv":
                            User.addUser(data[1], new Officer(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing user's data in the corresponding CSV file.
     *
     * @param user The user whose data is to be updated.
     */
    @Override
    public void updateUser(User user) {
        String filePath = "./bto/Data/CSV/" + user.getClass().getSimpleName() + "List.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            StringBuilder updatedContent = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (first) {
                        updatedContent.append(line).append("\n");
                        first = false;
                    } else {
                        String[] data = line.split(",");
                        if (data[1].equals(user.getNric())) {
                            updatedContent.append(user.toString()).append("\n");
                        } else {
                            updatedContent.append(line).append("\n");
                        }
                    }
                }
            }

            // Write the updated content back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(updatedContent.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
