package bto.Model;

/**
 * Enum representing the status of an application.
 */
public enum ApplicationStatus {
    /**
     * The application is pending and has not yet been processed.
     */
    PENDING,

    /**
     * The application has been successfully processed.
     */
    SUCCESS,

    /**
     * The application was unsuccessful.
     */
    UNSUCCESSFUL,

    /**
     * The application has been booked.
     */
    BOOKED;

    /**
     * Checks if the application status is terminal.
     * A terminal status means no further updates can be made to the application.
     *
     * @return {@code true} if the status is terminal (e.g., UNSUCCESSFUL), {@code false} otherwise.
     */
    public boolean isTerminal() {
        return this == UNSUCCESSFUL;
    }
}