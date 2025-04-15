package bto.Model;
import bto.Model.*;

public class Manager extends User{
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;

    public Manager(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }
}