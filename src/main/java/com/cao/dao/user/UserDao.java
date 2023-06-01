package com.cao.dao.user;

import com.cao.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //获取pojo文件内的实体类
    public User getLoginUser(Connection connection, String useCode) throws Exception;

    public int updatePassword(Connection connection, int id, String password) throws Exception;

    public int getUserCount(Connection connection, String userName, int userRole) throws Exception;

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPage, int pageSize) throws Exception;

    public int add(Connection connection, User user) throws Exception;

    public boolean userCodeExist(Connection connection, String userCode) throws Exception;
}
