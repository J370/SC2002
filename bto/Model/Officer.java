package bto.Model;

public class Officer extends User{

    private Project project;
    private String registrationStatus;

    public Officer(String name, String nric, int age, String maritalStatus, String password, Project project, String registrationStatus) {
        super(name, nric, age, maritalStatus, password);

        this.project = project;
        this.registrationStatus = "Not Registered";
    }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public void setRegistrationStatus(String registrationStatus) { this.registrationStatus = registrationStatus; }
    public String getRegistrationStatus() { return registrationStatus; }


}