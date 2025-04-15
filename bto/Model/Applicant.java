package bto.Model;

public class Applicant extends User{
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;

    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    public String getName() {return name;}
    public String getNric() {return nric;}
    public int getAge() {return age;}
    public String getMaritalStatus() {return maritalStatus;}
    public String getPassword() {return password;}
    public void setName(String name) {this.name = name;}
    public void setNric(String nric) {this.nric = nric;}
    public void setAge(int age) {this.age = age;}
    public void setMaritalStatus(String maritalStatus) {this.maritalStatus = maritalStatus;}
}