package bto.Model;


public enum ApplicationStatus {
    PENDING,
    SUCCESS,
    UNSUCCESSFUL,
    BOOKED;

    public boolean isTerminal() {
        return this == UNSUCCESSFUL;
    }
}