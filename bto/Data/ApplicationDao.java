package bto.Data;

import bto.Model.Application;
import java.util.List;
import java.util.Optional;

public interface ApplicationDao {
    void save(Application application);
    void update(Application application);
    void delete(String applicationId);
    Optional<Application> findById(String applicationId);
    Optional<Application> getActiveApplication(String applicantNric);
    List<Application> getApplicationsByStatus(String status);
    List<Application> getApplicationsByProject(String projectName);
}