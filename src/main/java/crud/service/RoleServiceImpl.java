package crud.service;

import crud.dao.RoleDao;
import crud.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public List<Role> listRoles() {
        return roleDao.listRoles();
    }

    @Override
    @Transactional
    public Role getRole(Integer id) {
        return roleDao.getRole(id);
    }

    @Override
    @Transactional
    public Role getRole(String name) {
        return roleDao.getRole(name);
    }

    @Override
    @Transactional
    public Integer addRole(Role role) {
        return roleDao.addRole(role);
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    @Transactional
    public void removeRole(Integer id) {
        roleDao.removeRole(id);
    }

    @Override
    @Transactional
    public void clearRoles() {
        roleDao.clearRoles();
    }
}
