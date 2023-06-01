package com.cao.dao.role;

import com.cao.dao.BaseDao;
import com.cao.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoIml implements RoleDao{

    @Override
    public List<Role> getRoleList(Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        List<Role> roleList = null;
        if (connection != null) {
            roleList = new ArrayList<>();
            ResultSet resultSet = null;
            String sql = "select * from smbms.smbms_role";
            Object[] params = {};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            while (resultSet.next()) {
                Role _role = new Role();
                _role.setId(resultSet.getInt("id"));
                _role.setRoleName(resultSet.getString("roleName"));
                _role.setRoleCode(resultSet.getString("roleCode"));
                roleList.add(_role);
            }
            BaseDao.closeResource(null, preparedStatement, resultSet);
        }
        return roleList;
    }
}
