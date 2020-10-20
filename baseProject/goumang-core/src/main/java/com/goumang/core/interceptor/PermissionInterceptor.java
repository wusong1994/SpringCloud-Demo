package com.goumang.core.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.goumang.core.annotation.NoLogin;
import com.goumang.core.annotation.NoPermit;
import com.goumang.core.util.ErrorUtil;
import com.goumang.core.util.SessionUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 权限拦截器<br>
 * 登录名为<b>admin</b>则不拦截，标注了{@link NoLogin}和{@link NoPermit}的方法或类不拦截
 * @author hrb
 * @since 1.0
 */
public class PermissionInterceptor implements HandlerInterceptor {

    /**
     * 接口前缀
     */
    private String prefix;

    /**
     * 过滤器，参数为登录用户信息
     */
    private Predicate<Map<String,Object>> predicate;

    /**
     * 登录名的字段名称
     * 通过该属性从session当中获取用户的名称
     */
    private String loginName;

    public PermissionInterceptor() {
        this.prefix = "console";
        this.loginName = "loginName";
    }

    public PermissionInterceptor(String prefix) {
        this.prefix = prefix;
        this.loginName = "loginName";
    }

    public PermissionInterceptor(String prefix, String loginName) {
        this.prefix = prefix;
        this.loginName = loginName;
    }

    public void addFilter(Predicate<Map<String,Object>> predicate){
        this.predicate = predicate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Object user = SessionUtil.getUser();
        //过滤器
        if(user!=null && predicate!=null){
            Map<String,Object> map = BeanUtil.beanToMap(user);
            if(predicate.test(map)) return true;
        }

        //用户名为admin则为超级管理员，并拥有所有权限
        if(user!=null && "admin".equals(SessionUtil.getLoginName(loginName))) return true;

        //若该类标注了NoPermit则无需校验权限
        Class clazz = handlerMethod.getBean().getClass();
        NoPermit noPermit = (NoPermit) clazz.getAnnotation(NoPermit.class);
        if(noPermit!=null){
            return true;
        }

        //不用登录的也不校验权限
        Method method = handlerMethod.getMethod();
        NoLogin noLogin = method.getAnnotation(NoLogin.class);
        if(noLogin!=null) return true;
        NoPermit noPermit1 = method.getAnnotation(NoPermit.class);
        if(noPermit1!=null) return true;

        Annotation[] annotations =  method.getAnnotations();
        Annotation annotation = null;
        for(Annotation a : annotations){
            if(a.annotationType().equals(RequestMapping.class)){
                annotation = a;
                break;
            }
            RequestMapping requestMapping = a.annotationType().getAnnotation(RequestMapping.class);
            if(requestMapping != null){
                annotation = a;
                break;
            }
        }
        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        if(requestMapping==null) ErrorUtil.forbidden();

        String funcCode = null;
        String func = requestMapping.value()[0];
        Pattern pattern = Pattern.compile("(?<=/?"+prefix+"/)[a-zA-Z]{1,}/[a-zA-Z]{1,}");
        Matcher matcher = pattern.matcher(func);
        if(matcher.find()) funcCode = matcher.group();
        if(funcCode==null) ErrorUtil.forbidden();

        Map<String, List<String>> permissions = SessionUtil.getPermissions();
        if(permissions==null || !permissions.containsKey(funcCode)) ErrorUtil.forbidden();

        String methodPath = "*";
        if(annotation!=null){
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) field.get(invocationHandler);
            String[] values = (String[]) memberValues.get("value");
            if(values!=null && values.length>0) methodPath = values[0];
        }
        if(methodPath.startsWith("/")) methodPath = methodPath.substring(1);
        String actionCode = request.getMethod().toUpperCase() + " " + methodPath;
        List<String> set = permissions.get(funcCode);
        if(set==null || set.size()<=0) ErrorUtil.forbidden();
        if(!set.contains(actionCode)) ErrorUtil.forbidden();

        return true;
    }
}
