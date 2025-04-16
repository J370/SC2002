package bto.Data;

import bto.Model.Enquiry;
import java.util.List;

public interface EnquiryDao {
    void save(Enquiry enquiry);
    void update(Enquiry enquiry);
    void delete(int enquiryId);
    Enquiry findById(int enquiryId);
    List<Enquiry> getEnquiriesByApplicant(String applicantNric);
    List<Enquiry> getEnquiriesByProject(String projectName);
    List<Enquiry> getAllEnquiries();
}