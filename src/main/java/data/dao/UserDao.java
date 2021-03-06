package data.dao;

import data.model.User;
import java.util.List;

public interface UserDao {
    List<User> listUsers();
    User getUser(Long id);
    Long addUser(User user);
    void updateUser(User user);
    void removeUser(Long id);
    void clearUsers();
}
