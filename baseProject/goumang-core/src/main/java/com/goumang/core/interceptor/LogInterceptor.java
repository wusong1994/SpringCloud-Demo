package com.goumang.core.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器<br>
 * 通过属性 {@link LogInterceptor#intercept} 设置拦截或不拦截所有方法，默认为不拦截<br>
 * 使用注解 {@link com.goumang.core.annotation.Log}、{@link com.goumang.core.annotation.NoLog} 拦截或不拦截标注的方法<br>
 * @author hrb
 * @since 1.0
 */
public class LogInterceptor implements HandlerInterceptor {

    private boolean intercept;

    public LogInterceptor() {
        this.intercept = false;
    }

    public LogInterceptor(boolean intercept) {
        this.intercept = intercept;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //TODO
        return true;
    }

}
