package com.goumang.sys.api.util;

import com.goumang.core.annotation.NoLogin;
import com.goumang.core.annotation.NoPermit;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * By huang.rb on 2019/9/2
 */
public class SysFuncUtil {

    /**
     * 获取功能点
     * @param handlerMethods 通过RequestMappingHandlerMapping#getHandlerMethods 获取
     * @param prefix 前缀
     * @return 功能点集合 key-funcCode,value-第一个值为method,第二个值为actionCode,第三个值0表示删除，1表示插入,第4个值为prefix
     */
    public static Map<String, List<String[]>> getFuncMap(Map<RequestMappingInfo, HandlerMethod> handlerMethods, String prefix){
        Map<String, List<String[]>> funcMap = new HashMap<>();
        Pattern funcPattern = Pattern.compile("(?<=/?"+prefix+"/)[a-zA-Z_]{1,15}/[a-zA-Z_]{1,}");
        Pattern actionPattern = Pattern.compile("(?<=/?"+prefix+"/[a-zA-Z_]{1,15}/[a-zA-Z_]{1,15}/)[a-zA-Z_\\{\\}/]{1,}");

        for( RequestMappingInfo info : handlerMethods.keySet()){

            Set<String> uriSet = info.getPatternsCondition().getPatterns();
            if(uriSet.isEmpty()) continue;
            String uri = uriSet.iterator().next();
            if(!uri.startsWith("/"+prefix)) continue;

            Set<RequestMethod> methodSet = info.getMethodsCondition().getMethods();
            String methodName = methodSet.isEmpty() ? "POST" : methodSet.iterator().next().name();

            HandlerMethod handlerMethod = handlerMethods.get(info);
            Method method = handlerMethod.getMethod();

            Matcher matcher = funcPattern.matcher(uri);
            if(!matcher.find()) continue;
            String funcCode = matcher.group();
            if(!funcMap.containsKey(funcCode)) funcMap.put(funcCode,new ArrayList<>());

            matcher = actionPattern.matcher(uri);
            String actionCode = matcher.find() ? matcher.group() : "*";

            String[] actionArr = new String[]{methodName, actionCode , "1", prefix};
            //无需登录或无需权限
            NoLogin noLogin = method.getAnnotation(NoLogin.class);
            NoPermit noPermit = method.getAnnotation(NoPermit.class);
            if(noLogin!=null || noPermit!=null){
                actionArr[2] = "0";
            }
            funcMap.get(funcCode).add(actionArr);
        }
        return funcMap;
    }
}
