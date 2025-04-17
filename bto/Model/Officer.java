package bto.Model;
import bto.Model.*;

public class Officer extends User{

    private Project project;
    private String status;
    private String registrationStatus;

    public Officer(String name, String nric, int age, String maritalStatus, String password, Project project, String status) {
        super(name, nric, age, maritalStatus, password);

        this.project = project;
        this.status = status;
        this.registrationStatus = "Not Registered";
    }

    public Project getProject() { return project; }
    public void setProject() { this.project = project; }
    public void setRegistrationStatus(String status) { this.status = status; }
    public void registerForProject(Project project) { this.registrationStatus = "Pending"; }

}