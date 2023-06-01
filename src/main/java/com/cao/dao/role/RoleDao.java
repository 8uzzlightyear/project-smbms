package com.cao.dao.role;

import com.cao.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {
    public List<Role> getRoleList(Connection connection) throws Exception;
}
