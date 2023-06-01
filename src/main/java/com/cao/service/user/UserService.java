package com.cao.service.user;

import com.cao.pojo.Role;
import com.cao.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //用户名和密码匹配
    public User login(String userCode, String usePassword);

    public boolean updatePassword(int id, String password);

    public int getUserCount(String userName, int userRole);

    public List<User> getUserList(String userName, int userRole, int currentPage, int pageSize);

    public boolean add(User user);

    public boolean userCodeExist(String userCode) throws SQLException;

}
