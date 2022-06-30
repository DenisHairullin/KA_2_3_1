package crud.data.service;

import crud.data.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findById(Long id);
    Iterable<User> findAll();
    User save(User user);
    void deleteById(Long id);
    void deleteAll();
}
