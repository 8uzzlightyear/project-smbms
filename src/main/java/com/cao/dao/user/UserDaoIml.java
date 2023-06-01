package com.cao.dao.user;

import com.cao.dao.BaseDao;
import com.cao.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//负责操作用户数据库
public class UserDaoIml implements UserDao{


    @Override
    public boolean userCodeExist(Connection connection, String userCode) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean flag = false;
        if (connection != null) {
            String sql = "select * from smbms.smbms_user where userCode=?";
            Object[] params = {userCode};
            //调用BaseDao类获得查询结果
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (resultSet.next()) {
                flag = true;
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return flag;
    }

    @Override
    public int add(Connection connection, User user) throws Exception {
        PreparedStatement preparedStatement = null;
        int count = 0;
        if (connection != null) {
            String sql = "insert into smbms.smbms_user (userCode,userName,userPassword, " +
                    " userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    " values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getUserRole(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            count = BaseDao.execute(connection, preparedStatement, sql, params);

            BaseDao.closeResource(null, preparedStatement, null);
        }
        return count;
    }

    public User getLoginUser(Connection connection, String useCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms.smbms_user where userCode=?";
            Object[] params = {useCode};
            //调用BaseDao类获得查询结果
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            } catch (SQLException e) {
                System.out.println("获取结果集失败");
                e.printStackTrace();
            }

            if (resultSet.next()) {
                System.out.println("获得用户");
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getTimestamp("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
            else System.out.println("用户不存在");

            BaseDao.closeResource(null, preparedStatement, resultSet);
        }
        return user;
    }

    public int updatePassword(Connection connection, int id, String password) throws Exception {
        System.out.println("update password");

        PreparedStatement preparedStatement = null;
        int count = 0;
        if (connection != null) {
            String sql = "update smbms.smbms_user set userPassword = ? where id = ?";
            Object[] params = {password, id};
            count = BaseDao.execute(connection, preparedStatement, sql, params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return count;
    }

    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement preparedStatement = null;
        int count = 0;

        if (connection != null) {
            ResultSet resultSet = null;
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as `count` from smbms.smbms_user u, smbms.smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), list.toArray());

            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPage, int pageSize) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();

        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*, r.roleName as userRoleName from smbms.smbms_user u, smbms.smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            sql.append(" order by creationDate DESC limit ?, ?");
            int currentPageFirstNo = (currentPage - 1) * pageSize;
            list.add(currentPageFirstNo);
            list.add(pageSize);
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), list.toArray());
            //这里有多个结果，用while
            while (resultSet.next()) {
                User _user = new User();
                _user.setId(resultSet.getInt("id"));
                _user.setUserCode(resultSet.getString("userCode"));
                _user.setUserName(resultSet.getString("userName"));
                _user.setGender(resultSet.getInt("gender"));
                _user.setBirthday(resultSet.getDate("birthday"));
                _user.setPhone(resultSet.getString("phone"));
                _user.setUserRole(resultSet.getInt("userRole"));
                _user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null, preparedStatement, resultSet);
        }
        return userList;
    }
}
