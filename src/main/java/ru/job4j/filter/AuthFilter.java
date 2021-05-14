package ru.job4j.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        String query = req.getQueryString() == null ? "" : req.getQueryString();
        if (
                (query.endsWith("login") || query.endsWith("registration"))
                        && req.getSession().getAttribute("user") != null
        ) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        if (query.endsWith("login") || query.endsWith("registration")
                || uri.endsWith("/photo") || query.endsWith("get-all-ads")
                || uri.endsWith("/") || uri.endsWith(".css") || uri.endsWith(".js")
                || uri.endsWith(".jpeg")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/user?page=login");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
