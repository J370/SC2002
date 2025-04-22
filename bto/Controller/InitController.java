package bto.Controller;

import bto.Data.*;

public class InitController {
    public void readCSV() {
        UserDao userDao = new UserCSVDao();

        userDao.readUsers("./bto/Data/CSV/ApplicantList.csv");
        userDao.readUsers("./bto/Data/CSV/ManagerList.csv");
        userDao.readUsers("./bto/Data/CSV/OfficerList.csv");
    }
}
