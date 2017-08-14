package com.heu.cs.poet.jettyserver;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by memgq on 2017/7/2.
 */
public class LoginFilter implements Filter {
    public LoginFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 登录过滤器，未登录的跳转到登录页面
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//        String path = ((HttpServletRequest) servletRequest).getRequestURI();
//        System.out.println(path);
//        if (path.indexOf("/login") > -1) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        Authentication authentication = new Authentication();
//        if (authentication.isLoged(httpRequest.getSession())) {
//            filterChain.doFilter(servletRequest, servletResponse);
//        } else {
//            httpResponse.sendRedirect("/webapi/user/login");
//        }
    }

    @Override
    public void destroy() {

    }
}
