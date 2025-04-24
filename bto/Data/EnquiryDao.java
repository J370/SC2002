package bto.Data;

import bto.Model.Enquiry;
import java.util.List;

/**
 * Data Access Object (DAO) interface for managing enquiry-related operations.
 */
public interface EnquiryDao {

    /**
     * Saves a new enquiry to the data source.
     *
     * @param enquiry The enquiry to save.
     */
    void save(Enquiry enquiry);

    /**
     * Updates an existing enquiry in the data source.
     *
     * @param enquiry The enquiry to update.
     */
    void update(Enquiry enquiry);

    /**
     * Deletes an enquiry from the data source by its ID.
     *
     * @param enquiryId The ID of the enquiry to delete.
     */
    void delete(int enquiryId);

    /**
     * Finds an enquiry by its ID.
     *
     * @param enquiryId The ID of the enquiry.
     * @return The enquiry if found, or {@code null} if not found.
     */
    Enquiry findById(int enquiryId);

    /**
     * Retrieves all enquiries submitted by a specific applicant.
     *
     * @param applicantNric The NRIC of the applicant.
     * @return A list of enquiries submitted by the applicant.
     */
    List<Enquiry> getEnquiriesByApplicant(String applicantNric);

    /**
     * Retrieves all enquiries for a specific project.
     *
     * @param projectName The name of the project.
     * @return A list of enquiries for the specified project.
     */
    List<Enquiry> getEnquiriesByProject(String projectName);

    /**
     * Retrieves all enquiries from the data source.
     *
     * @return A list of all enquiries.
     */
    List<Enquiry> getAllEnquiries();
}