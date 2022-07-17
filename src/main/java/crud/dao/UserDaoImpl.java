package crud.dao;

import crud.model.User;
import org.hibernate.annotations.QueryHints;
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
            return entityManager.createQuery("select u from User u left join fetch u.roles where u.email = :email",
                            User.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        return entityManager.createQuery("select distinct u from User u left join fetch u.roles order by u.id")
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void removeUser(Long id) {
        entityManager.createQuery("delete from User u where u.id = :id")
                .setParameter("id", id).executeUpdate();
    }

    @Override
    public void clearUsers() {
        entityManager.createQuery("delete from User").executeUpdate();
    }
}
