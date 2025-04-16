package bto.Data;

import bto.Model.Project;
import java.util.List;

public interface ProjectDao {
    List<Project> getAllProjects();
    Project getProjectById(String projectId);
    void updateProject(Project project);
    void decreaseAvailableUnits(String projectId, String flatType, int count);
}
