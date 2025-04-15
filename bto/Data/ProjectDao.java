package bto.Data;

import bto.Model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    List<Project> getAllProjects();
    Optional<Project> getProjectById(String projectId);
    void updateProject(Project project);
    void decreaseAvailableUnits(String projectId, String flatType, int count);
}
