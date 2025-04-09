package bto;
import bto.View.LoginView;
import bto.Controller.*;

public class App {
    public App() {

    }

    public static void main(String[] args) {
        AuthController authController = new AuthController();
        authController.readCSV();

        LoginView login = new LoginView();
        login.displayLoginPrompt();
    }
}
