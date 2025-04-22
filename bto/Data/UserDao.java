package bto.Data;

import bto.Model.User;
import java.util.List;

public interface UserDao {
    void readUsers(String filePath);
    void updateUser(User user);
}
