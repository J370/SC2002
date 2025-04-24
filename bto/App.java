package bto;
import bto.View.LoginView;
import bto.Controller.*;

/**
 * The App class serves as the entry point for the application.
 * It initializes the system and displays the login prompt to the user.
 */
public class App {

    /**
     * Default constructor for the App class.
     */
    public App() {
    }

    /**
     * The main method is the entry point of the application.
     * It initializes the system by reading data from CSV files and starts the login process.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        InitController initController = new InitController();
        initController.readCSV();

        LoginView login = new LoginView();
        login.displayLoginPrompt();
    }
}
