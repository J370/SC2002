package bto.Data;
import bto.Model.*;
import java.io.*;

public class UserCSVDao implements UserDao {
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

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(updatedContent.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
