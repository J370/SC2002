package bto.Controller;

import bto.Data.*;

public class InitController {
    public void readCSV() {
        UserCSVDao csvDao = new UserCSVDao();

        csvDao.readUsers("./bto/Data/CSV/ApplicantList.csv");
        csvDao.readUsers("./bto/Data/CSV/ManagerList.csv");
        csvDao.readUsers("./bto/Data/CSV/OfficerList.csv");
    }
}
