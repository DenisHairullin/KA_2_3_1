package data.service;

import data.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User getUser(Long id);
    List<User> listUsers();
    Long addUser(User user);
    void updateUser(User user);
    void removeUser(Long id);
    void clearUsers();
}
