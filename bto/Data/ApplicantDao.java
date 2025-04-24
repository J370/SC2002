package bto.Data;

import bto.Model.Applicant;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing applicant-related operations.
 */
public interface ApplicantDao {

    /**
     * Retrieves an applicant by their NRIC.
     *
     * @param nric The NRIC of the applicant.
     * @return An {@code Optional} containing the applicant if found, or empty if not found.
     */
    Optional<Applicant> getApplicantByNric(String nric);

    /**
     * Retrieves a list of all applicants.
     *
     * @return A list of all applicants.
     */
    List<Applicant> getAllApplicants();

    /**
     * Saves a new applicant to the data source.
     *
     * @param applicant The applicant to save.
     */
    void saveApplicant(Applicant applicant);

    /**
     * Updates an existing applicant in the data source.
     *
     * @param applicant The applicant to update.
     */
    void updateApplicant(Applicant applicant);
}