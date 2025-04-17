package bto.Data;

import bto.Model.Project;
import java.util.List;

public interface ProjectDao {
    void saveProject(Project project);
    void updateProject(Project project);
    void deleteProject(String projectId);
    List<Project> getAllProjects();
    Project getProjectById(String projectId);
    void decreaseAvailableUnits(String projectId, String flatType, int count);
}
