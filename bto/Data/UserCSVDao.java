package bto.Data;
import bto.Model.*;
import java.io.*;

public class UserCSVDao {
    public void readUsers(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                }
                else {
                    // use comma as separator
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
}
