package crud.service;

import crud.model.User;
import java.util.List;

public interface UserService {
    User getUser(Long id);
    List<User> listUsers();
    Long addUser(User user);
    void updateUser(User user);
    void removeUser(Long id);
    void clearUsers();
}
