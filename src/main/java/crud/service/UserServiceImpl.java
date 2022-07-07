package crud.service;

import crud.dao.UserDao;
import crud.model.User;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUser(username);
        if (user != null) {
            Hibernate.initialize(user.getRoles());
            return user;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Override
    @Transactional
    public Long addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    @Override
    @Transactional
    public void clearUsers() {
        userDao.clearUsers();
    }
}
