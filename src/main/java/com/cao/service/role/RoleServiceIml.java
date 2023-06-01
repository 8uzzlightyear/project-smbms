package com.cao.service.role;

import com.cao.dao.BaseDao;
import com.cao.dao.role.RoleDao;
import com.cao.dao.role.RoleDaoIml;
import com.cao.pojo.Role;
import com.cao.service.user.UserService;
import com.cao.service.user.UserServiceIml;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class RoleServiceIml implements RoleService{
    private RoleDao roleDao;

    public RoleServiceIml() {
        roleDao = new RoleDaoIml();
    }

    @Override
    public List<Role> getRoleList() throws Exception {
        Connection connection = null;
        List<Role> roleList;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }

    @Test
    public void test() throws Exception {
        RoleService roleService = new RoleServiceIml();
        List<Role> roleList = roleService.getRoleList();
        for (Role role :
                roleList) {
            System.out.println(role.getRoleName());
        }
    }
}
