package bto.Data;

import bto.Model.User;

public interface UserDao {
    void readUsers(String filePath);
    void updateUser(User user);
}
