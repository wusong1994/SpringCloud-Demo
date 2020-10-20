package com.goumang.core.interceptor;


import com.goumang.core.annotation.Login;
import com.goumang.core.annotation.NoLogin;
import com.goumang.core.base.BaseException;
import com.goumang.core.base.BaseExceptionEnum;
import com.goumang.core.util.SessionUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 登录拦截器<br>
 * 通过属性 {@link LoginInterceptor#intercept} 设置拦截或不拦截所有方法，默认为不拦截<br>
 * 使用注解 {@link Login}、{@link NoLogin} 拦截或不拦截标注的方法<br>
 * @author hrb
 * @since 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {

    private boolean intercept;

    public LoginInterceptor() {
        this.intercept = false;
    }

    public LoginInterceptor(boolean intercept) {
        this.intercept = intercept;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断接口是否需要登录
        if(intercept){
            NoLogin noLogin = method.getAnnotation(NoLogin.class);
            if(noLogin != null) return true;
        }else{
            Login login = method.getAnnotation(Login.class);
            if(login == null) return true;
        }

        Object user = SessionUtil.getUser();
        if (user != null) return true;

        throw new BaseException(BaseExceptionEnum.UNAUTHORIZED);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
