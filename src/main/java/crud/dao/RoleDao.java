package crud.dao;

import crud.model.Role;

import java.util.List;

public interface RoleDao {
    List<Role> listRoles();
    Role getRole(Integer id);
    Role getRole(String name);
    Integer addRole(Role role);
    void updateRole(Role role);
    void removeRole(Integer id);
    void clearRoles();
}
