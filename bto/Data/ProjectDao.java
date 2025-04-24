package bto.Data;

import bto.Model.Project;
import java.util.List;

/**
 * Data Access Object (DAO) interface for managing project-related operations.
 */
public interface ProjectDao {

    /**
     * Saves a new project to the data source.
     *
     * @param project The project to save.
     */
    void saveProject(Project project);

    /**
     * Updates an existing project in the data source.
     *
     * @param project The project to update.
     */
    void updateProject(Project project);

    /**
     * Deletes a project from the data source by its name.
     *
     * @param projectId The name of the project to delete.
     */
    void deleteProject(String projectId);

    /**
     * Retrieves all projects from the data source.
     *
     * @return A list of all projects.
     */
    List<Project> getAllProjects();

    /**
     * Retrieves a project by its name.
     *
     * @param projectId The name of the project.
     * @return The project if found.
     */
    Project getProjectById(String projectId);

    /**
     * Decreases the available units for a specific flat type in a project.
     *
     * @param projectId The name of the project.
     * @param flatType The flat type to update.
     * @param count The number of units to decrease.
     */
    void decreaseAvailableUnits(String projectId, String flatType, int count);
}
