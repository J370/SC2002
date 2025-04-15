package bto.Data;

import bto.Model.Applicant;
import java.util.List;
import java.util.Optional;

public interface ApplicantDao {
    Optional<Applicant> getApplicantByNric(String nric);
    List<Applicant> getAllApplicants();
    void saveApplicant(Applicant applicant);
    void updateApplicant(Applicant applicant);
}