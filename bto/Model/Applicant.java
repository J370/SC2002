package bto.Model;

/**
 * Represents an applicant in the system.
 * An applicant is a type of user who can apply for housing projects.
 */
public class Applicant extends User {

    /**
     * Constructs an Applicant with the specified details.
     *
     * @param name The name of the applicant.
     * @param nric The NRIC of the applicant.
     * @param age The age of the applicant.
     * @param maritalStatus The marital status of the applicant.
     * @param password The password for the applicant's account.
     */
    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }
}