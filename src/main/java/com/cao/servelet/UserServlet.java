package com.cao.servelet;

import com.alibaba.fastjson.JSONArray;
import com.cao.pojo.Role;
import com.cao.pojo.User;
import com.cao.service.role.RoleService;
import com.cao.service.role.RoleServiceIml;
import com.cao.service.user.UserService;
import com.cao.service.user.UserServiceIml;
import com.cao.util.Constants;
import com.cao.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        System.out.println("method----> " + method);
        if (method != null && method.equals("savepwd")) {
            this.updatePassword(req, resp);
        }else if( method != null && method.equals("pwdmodify")) {
            this.pwdmodifyd(req, resp);
        }else if( method != null && method.equals("query")) {
            this.query(req, resp);
        }else if( method != null && method.equals("add")) {
            this.add(req, resp);
        } else if (method != null && method.equals("getrolelist")) {
            this.getRoleList(req, resp);
        } else if (method != null && method.equals("ucexist")) {
            this.userCodeExist(req,resp);
        }
    }

    public void userCodeExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userCode = request.getParameter("userCode");
        if (userCode == null) {
            userCode = "";
        }
        System.out.println(userCode);
        UserService userService = new UserServiceIml();
        Map<String,String> map = new HashMap<>();
        map.put("userCode", "NotExist");
        boolean flag = false;
        try {
            flag = userService.userCodeExist(userCode);
        } catch (SQLException e) {
            System.out.println("查找失败");
            throw new RuntimeException(e);
        }
        if (flag) {
            System.out.println("exist");
            map.put("userCode", "exist");
            //request.setAttribute("userCode", "exist");
        }else {
            System.out.println("NotExist");
            //request.setAttribute("userCode", "notExist");
        }
        PrintWriter writer = response.getWriter();
        writer.write(JSONArray.toJSONString(map));
        writer.close();
    }

    public void getRoleList(HttpServletRequest request, HttpServletResponse response) {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceIml();
        try {
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (roleList != null) {
            try {
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(JSONArray.toJSONString(roleList));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        UserService userService = new UserServiceIml();
        if (userService.add(user)) {
            response.sendRedirect("userlist.jsp");
        }else {
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }
    }

    public void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前端参数
        String queryName = request.getParameter("queryname");
        String temp = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");
        UserService userService = new UserServiceIml();
        List<User> userList;
        int queryUserRole = 0;
        int currentPageNo = 1;
        int pageSize = 5;
        if (queryName == null) {
            queryName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        int totalCount = userService.getUserCount(queryName, queryUserRole);

        //页数处理主持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        userList = userService.getUserList(queryName, queryUserRole, currentPageNo, pageSize);
        request.setAttribute("userList", userList);

        //获取角色列表
        RoleService roleService = new RoleServiceIml();
        List<Role> roleList;
        try {
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("roleList", roleList);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPageNo", currentPageNo);
        request.setAttribute("totalPageCount", totalPageCount);
        request.setAttribute("queryUserName", queryName);
        request.setAttribute("queryUserRole", queryUserRole);

        //返回前端
        request.getRequestDispatcher("userlist.jsp").forward(request, response);
    }

    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        String newPassword = request.getParameter("newpassword");
        System.out.println("newPassword" + newPassword);
        boolean flag = false;

        if (user != null && !StringUtils.isNullOrEmpty(newPassword)) {
            UserService userService = new UserServiceIml();
            flag = userService.updatePassword(user.getId(), newPassword);
            if (flag) {
                request.setAttribute(Constants.SYS_MESSAGE, "密码修改成功");
                //密码修改成功，注销session，重新登录
                request.getSession().removeAttribute(Constants.USER_SESSION);
            }
            else {
                request.setAttribute(Constants.SYS_MESSAGE, "密码修改失败");
            }
        }
        else {
            request.setAttribute(Constants.SYS_MESSAGE, "会话过期");
        }
        request.getRequestDispatcher("pwdmodify.jsp").forward(request, response);
    }

    public void pwdmodifyd(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = request.getParameter("oldpassword");
        HashMap<String, String> resultMap = new HashMap<>();
        if (user != null && oldpassword.equals(user.getUserPassword())) {
            resultMap.put("result", "true");
        } else if (user != null && !oldpassword.equals(user.getUserPassword())) {
            resultMap.put("result", "false");
        } else if (user == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        }

        try {
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            //PrintWrite 对象不需要flush方法刷新
            //writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
