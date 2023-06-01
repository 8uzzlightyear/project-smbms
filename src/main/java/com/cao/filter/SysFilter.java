package com.cao.filter;

import com.cao.pojo.User;
import com.cao.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("--------SysFilter---------");
        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        HttpServletResponse rsp = (HttpServletResponse) servletResponse;
        User user = (User) rq.getSession().getAttribute(Constants.USER_SESSION);
        if (user == null) {
            rsp.sendRedirect(rq.getContextPath() + "/error.jsp");
        }
        else {
            filterChain.doFilter(rq,rsp);
        }
    }

    public void destroy() {

    }
}
