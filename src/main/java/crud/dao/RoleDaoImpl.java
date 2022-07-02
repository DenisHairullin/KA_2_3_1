package crud.dao;

import crud.model.Role;
import org.hibernate.NonUniqueResultException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRole(Integer id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role getRole(String name) {
        try {
            return (Role) entityManager.createQuery("select r from Role r where r.name = :name")
                    .setParameter("name", name).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> listRoles() {
        return entityManager.createQuery("select r from Role r").getResultList();
    }

    @Override
    public Integer addRole(Role role) {
        entityManager.persist(role);
        return role.getId();
    }

    @Override
    public void updateRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void removeRole(Integer id) {
        entityManager.remove(id);
    }

    @Override
    public void clearRoles() {
        entityManager.createQuery("delete from Role").executeUpdate();
    }
}
