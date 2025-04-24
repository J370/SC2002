package bto.Controller;

import bto.Data.*;

/**
 * Controller class for initializing the application by reading data from CSV files.
 */
public class InitController {

    /**
     * Reads user data from predefined CSV files and loads it into the system.
     */
    public void readCSV() {
        UserDao userDao = new UserCSVDao();

        // Read user data from CSV files
        userDao.readUsers("./bto/Data/CSV/ApplicantList.csv");
        userDao.readUsers("./bto/Data/CSV/ManagerList.csv");
        userDao.readUsers("./bto/Data/CSV/OfficerList.csv");
    }
}
