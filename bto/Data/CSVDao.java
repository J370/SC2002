package bto.Data;
import java.io.*;
import java.util.*;
import bto.Model.*;

public class CSVDao {
    public void readUser(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(",");
                switch (filePath) {
                    case "ApplicantList.csv":
                        Applicant.add(new Applicant(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]));
                        break;
                    // case "ManagerList.csv":
                    //     Manager.add(new Manager(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]));
                    //     break;
                    // case "OfficerList.csv":
                    //     Officer.add(new Officer(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4]));
                    //     break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
