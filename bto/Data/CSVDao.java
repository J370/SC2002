package bto.Data;
import java.io.*;
import java.util.*;
import bto.Model.*;

public class CSVDao {
    public void readUser(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Applicant applicant = null;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                }
                else {
                    // use comma as separator
                    String[] data = line.split(",");
                    filePath = filePath.replace("./bto/Data/", "");
                    switch (filePath) {
                        case "ApplicantList.csv":
                            new Applicant(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]);
                            break;
                        // case "ManagerList.csv":
                        //     new Manager(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]);
                        //     break;
                        // case "OfficerList.csv":
                        //     new Officer(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]);
                        //     break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
