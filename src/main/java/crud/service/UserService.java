package crud.service;

import crud.model.User;
import java.util.List;

public interface UserService {
    User getUser(Long id);
    User getUser(String login);
    List<User> listUsers();
    void addUser(User user);
    void updateUser(User user);
    void removeUser(Long id);
    void clearUsers();
}
