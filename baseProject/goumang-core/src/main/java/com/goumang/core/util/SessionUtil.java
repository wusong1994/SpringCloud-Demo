package com.goumang.core.util;

import com.goumang.core.base.BasePo;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 *
 * By huang.rb on 2019/4/3
 */
public class SessionUtil{

    public final static String CURR_USER = "_currentUser";

    public final static String CURR_PERMISSIONS = "_currentPermissions";

    /**
     * 获取登录用户
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getUser(Class<T> clazz){
        T user = (T)getSession().getAttribute(CURR_USER);
        return user;
    }

    public static Object getUser(){
        return getSession().getAttribute(CURR_USER);
    }

    public static Object getUserId(){
        BasePo user = getUser(BasePo.class);
        return user==null ? null : user.getPk();
    }

    public static <T> T getUserId(Class<T> clazz){
        Object id = getUserId();
        if(id == null) return null;
        T t = clazz.cast(id);
        return t;
    }

    public static String getLoginName(String field){
        try {
            Object user = SessionUtil.getUser();
            PropertyDescriptor pd = new PropertyDescriptor(field, user.getClass());
            Method getMethod = pd.getReadMethod();
            String value = (String)getMethod.invoke(user);
            return value;
        } catch (Exception e) {
            ErrorUtil.error(e.getMessage());
        }
        return null;
    }

    public static void setUser(Object obj){
        getSession().setAttribute(CURR_USER,obj);
    }

    public static void setPermissions(Map<String, List<String>> permissions){
        getSession().setAttribute(CURR_PERMISSIONS,permissions);
    }

    public static Map<String,List<String>> getPermissions(){
        Map<String,List<String>> permissions = (Map<String,List<String>>) getSession().getAttribute(CURR_PERMISSIONS);
        return permissions;
    }

    public static void logout(){
        getSession().removeAttribute(CURR_USER);
        getSession().invalidate();
    }

    public static void setAttribute(String name, Object value){
        HttpSession session = getSession();
        session.setAttribute(name, value);
    }

    public static Object getAttribute(String name){
        HttpSession session = getSession();
        return session.getAttribute(name);
    }

    public static void removeAttribute(String name){
        HttpSession session = getSession();
        session.removeAttribute(name);
    }

    public static HttpSession getSession() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        return sra.getRequest().getSession();
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        return sra.getResponse();
    }


    public static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        return sra.getRequest();
    }

    public static String getPath(){
        String path = (String) getRequest().getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) getRequest().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }

    public static String getRequestIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
