package crud.dao;

import crud.model.User;
import java.util.List;

public interface UserDao {
    List<User> listUsers();
    User getUser(Long id);
    User getUser(String email);
    Long addUser(User user);
    void updateUser(User user);
    void removeUser(Long id);
    void clearUsers();
}
