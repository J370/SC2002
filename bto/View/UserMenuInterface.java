package bto.View;

/**
 * The UserMenuInterface defines a contract for user menu views.
 * Classes implementing this interface must provide a method to display a menu.
 */
public interface UserMenuInterface {

    /**
     * Displays the menu for the user and handles user input for menu options.
     *
     * @param isFirstTime Indicates if this is the first time the menu is being displayed.
     */
    void displayMenu(boolean isFirstTime);
}