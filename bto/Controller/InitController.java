package bto.Controller;

import bto.Data.*;

public class InitController {
    public void readCSV() {
        UserCSVDao csvDao = new UserCSVDao();

        csvDao.readUser("./bto/Data/ApplicantList.csv");
        // csvDao.readCSV("ManagerList.csv");
        // csvDao.readCSV("OfficerList.csv");
    }
}
