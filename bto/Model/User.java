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
    
    public static User getUser(String id) {
        return users.get(id);
    }

    public static HashMap<String, User> getAllUsers() {
        return users;
    }

    public String getPassword() {
        return password;
    }
}
