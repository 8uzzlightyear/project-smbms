package com.cao.servelet;

import com.cao.pojo.User;
import com.cao.service.user.UserService;
import com.cao.service.user.UserServiceIml;
import com.cao.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    //servlet:控制层，调用业务层

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("--------LoginServlet----------");
        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //调用业务层进行用户密码匹配
        UserService userService = new UserServiceIml();
        User user = userService.login(userCode, userPassword);

        if (user != null) { //登录成功
            //将用户信息放入session,只要不注销或session不过期，用户信息便可通过获取
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            //页面跳转——frame.jsp
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            //页面跳转——login.jsp
            req.setAttribute("error", "用户名或密码错误！");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
