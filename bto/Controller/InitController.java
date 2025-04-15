package bto.Controller;

import bto.Data.*;

public class InitController {
    public void readCSV() {
        UserCSVDao csvDao = new UserCSVDao();

        csvDao.readUsers("./bto/Data/ApplicantList.csv");
        csvDao.readUsers("./bto/Data/ManagerList.csv");
        csvDao.readUsers("./bto/Data/OfficerList.csv");
    }
}
