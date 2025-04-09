package bto.Controller;

import bto.Data.*;

public class AuthController {
    public static void readCSV() {
        CSVDao csvDao = new CSVDao();

        csvDao.readUser("./bto/Data/ApplicantList.csv");
        // csvDao.readCSV("ManagerList.csv");
        // csvDao.readCSV("OfficerList.csv");
    }
}
