package bto.Data;

import bto.Model.Application;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing application-related operations.
 */
public interface ApplicationDao {

    /**
     * Saves a new application to the data source.
     *
     * @param application The application to save.
     */
    void save(Application application);

    /**
     * Updates an existing application in the data source.
     *
     * @param application The application to update.
     */
    void update(Application application);

    /**
     * Deletes an application from the data source by its ID.
     *
     * @param applicationId The ID of the application to delete.
     */
    void delete(String applicationId);

    /**
     * Retrieves an application by its ID.
     *
     * @param applicationId The ID of the application.
     * @return An {@code Optional} containing the application if found, or empty if not found.
     */
    Optional<Application> getApplicationById(String applicationId);

    /**
     * Retrieves the active application for a specific applicant by their NRIC.
     *
     * @param applicantNric The NRIC of the applicant.
     * @return An {@code Optional} containing the active application if found, or empty if not found.
     */
    Optional<Application> getActiveApplication(String applicantNric);

    /**
     * Retrieves all applications with a specific status.
     *
     * @param status The status to filter applications by.
     * @return A list of applications with the specified status.
     */
    List<Application> getApplicationsByStatus(String status);

    /**
     * Retrieves all applications for a specific project.
     *
     * @param projectName The name of the project.
     * @return A list of applications for the specified project.
     */
    List<Application> getApplicationsByProject(String projectName);

    /**
     * Retrieves all applications from the data source.
     *
     * @return A list of all applications.
     */
    List<Application> getAllApplications();
}