package bto.Model;
import java.util.HashMap;

public abstract class User {
    private static HashMap<String, User> users = new HashMap<>();
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;

    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    public static void addUser(String id, User user) {
        users.put(id, user);
    }
    
    public static User getUser(String id) {return users.get(id);}
    public static HashMap<String, User> getAllUsers() {return users;}

    public String getName() {return name;}
    public String getNric() {return nric;}
    public int getAge() {return age;}
    public String getMaritalStatus() {return maritalStatus;}
    public String getPassword() {return password;}
    public void setName(String name) {this.name = name;}
    public void setNric(String nric) {this.nric = nric;}
    public void setAge(int age) {this.age = age;}
    public void setMaritalStatus(String maritalStatus) {this.maritalStatus = maritalStatus;}
    public void setPassword(String password) {this.password = password;}

    public String toString() {
        return name + ',' + nric + "," + age + "," + maritalStatus + "," + password;
    }

}
