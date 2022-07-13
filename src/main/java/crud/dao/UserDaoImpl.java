package crud.dao;

import crud.model.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUser(String email) {
        try {
            return (User) entityManager.createQuery("select u from User u where u.email = :email")
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Override
    public Long addUser(User user) {
        entityManager.persist(user);
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void removeUser(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public void clearUsers() {
        entityManager.createQuery("delete from User").executeUpdate();
    }
}
