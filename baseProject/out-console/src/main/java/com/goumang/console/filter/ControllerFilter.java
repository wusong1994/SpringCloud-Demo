package com.goumang.console.filter;

import com.goumang.core.web.ParameterTrimRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/console/*")
public class ControllerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new ParameterTrimRequest((HttpServletRequest) servletRequest), servletResponse);
    }

    @Override
    public void destroy() {

    }
}
