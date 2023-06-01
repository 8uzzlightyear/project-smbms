package com.cao.service.user;

import com.cao.dao.BaseDao;
import com.cao.dao.user.UserDao;
import com.cao.dao.user.UserDaoIml;
import com.cao.pojo.Role;
import com.cao.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//用户服务类
public class UserServiceIml implements UserService {
    //使用用户服务大概率涉及数据库用户表操作，将其作为用户服务私有属性，提高作用域
    private UserDao userDao;
    public UserServiceIml() {
        userDao = new UserDaoIml();
    }

    @Override
    public boolean userCodeExist(String userCode) {
        Connection connection = null;
        boolean flag = false;
        connection = BaseDao.getConnection();
        try {
            flag = userDao.userCodeExist(connection, userCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BaseDao.closeResource(connection,null,null);
        return flag;
    }

    //验证登录账户和密码，返回用户对象
    public User login(String userCode, String userPassword) {
        Connection connection = null;
        User user = null;
        //通过数据库操作类得到连接器
        connection = BaseDao.getConnection();
        //通过用户操作类获得用户
        try {
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            System.out.println("用户查找失败");
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }

        if (user != null) {
            if (!user.getUserPassword().equals(userPassword)) {
                System.out.println("密码错误");
                user = null;
            }
        }
        return user;
    }

    public boolean updatePassword(int id, String password) {
        System.out.println("UserService" + password);

        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePassword(connection, id, password) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String userName, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return count;
    }

    @Override
    public boolean add(User user) {
        boolean flag;
        Connection connection = null;
        int count = 0;
        if (user != null) {
            try {
                connection = BaseDao.getConnection();
                //开启事务
                connection.setAutoCommit(false);
                count = userDao.add(connection, user);
                connection.commit();
                System.out.println("提交成功，commit----------");
            } catch (Exception e) {
                System.out.println("提交修改失败，rollback----------");
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                BaseDao.closeResource(connection, null, null);
            }
        }
        if (count > 0) {
            flag = true;
        }else {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPage, int pageSize) {
        Connection connection = null;
        List<User> userList= null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, userName, userRole, currentPage, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

}
