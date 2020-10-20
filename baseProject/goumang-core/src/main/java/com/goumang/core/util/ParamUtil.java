package com.goumang.core.util;


import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * By huang.rb on 2018/12/25
 * 参数处理类
 */
public class ParamUtil {

    /**
     * 允许接受的参数
     * @param params 支持Map 或 实体
     * @param fields 允许接受的参数的字段名称
     */
    public static void accept(Object params,String... fields) {
        if(params instanceof Map){
            Map<String,Object> pa = (Map<String,Object>) params;
            pa.keySet().forEach(o->{
               if(!ArrayUtils.contains(fields,o)) pa.remove(o);
            });
        }else{
            try {
                Class clazz = params.getClass();
                Object temp = clazz.newInstance();
                Field[] fArr = clazz.getDeclaredFields();
                for(String field:fields){
                    PropertyDescriptor pd = new PropertyDescriptor(field,clazz);
                    Method setMethod = pd.getWriteMethod();
                    Method getMethod = pd.getReadMethod();
                    setMethod.setAccessible(true);
                    setMethod.invoke(temp,getMethod.invoke(params));
                }
                BeanUtils.copyProperties(temp,params);
            } catch (Exception e) {
                ErrorUtil.error(e.getMessage());
            }
        }
    }
    /**
     * 不允许接受的参数
     * @param params 支持Map 或 实体
     * @param fields 允许接受的参数的字段名称
     */
    public static void noAccept(Object params,String... fields){

        if(params instanceof Map){
            Map<String,Object> pa = (Map<String,Object>) params;
            for(String f:fields){
                if(pa.containsKey(f)) pa.remove(f);
            }
        }else{
            try {
                Class clazz = params.getClass();
                Object temp = clazz.newInstance();
                BeanUtils.copyProperties(params,temp,fields);
                BeanUtils.copyProperties(temp,params);
            } catch (Exception e) {
                ErrorUtil.error(e.getMessage());
            }
        }
    }

    /**
     * 参数验证
     * @param params 支持Map 或 实体
     * @param predicate  验证方法（接收一个对象，返回boolean）
     * @param keys 需要验证的key
     * @param msg 错误提示的信息
     * @throws Exception
     */
    public static  void valid(Object params, Predicate<Object> predicate, String[] keys, String msg){
        if(params==null) ErrorUtil.error("params must be null");
        List<String> err = new ArrayList<String>();
        if(params instanceof Map){
            Map<String,Object> pa = (Map<String,Object>) params;
            if(keys!=null){
                Arrays.stream(keys).forEach(o->{
                    if(!predicate.test(pa.get(o))) err.add(o);
                });
            }else{
                pa.keySet().forEach(o->{
                    if(!predicate.test(pa.get(o))) err.add(o);
                });
            }
        }else {
            try {
                for(String k :keys){
                    Field field = ReflectUtil.getField(params.getClass(),k);
                    Method m = null;
                    if(field!=null){
                        if(field.getType().equals(boolean.class)){
                            m = params.getClass().getMethod("is"+k.substring(0,1).toUpperCase()+k.substring(1));
                        }
                    }
                    if(m==null) m = params.getClass().getMethod("get"+k.substring(0,1).toUpperCase()+k.substring(1));

                    Object value = m.invoke(params);
                    if(!predicate.test(value)) err.add(k);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ErrorUtil.error(e.getMessage());
            }
        }
        if(!err.isEmpty()){
            ErrorUtil.error("["+StringUtils.join(err,",")+"] "+msg);
        }
    }

    /**
     * 非null校验
     * @param params
     * @param keys
     */
    public static void notNull(Object params,String... keys){
        valid(params,o -> o!=null,keys,"can't be null!");
    }

    /**
     * 非空校验 支持Map 或 实体
     * @param params
     * @param keys
     */
    public static void notBlank(Object params,String... keys){
        valid(params,o->{
          if(o instanceof String){
              return StringUtils.isNotBlank((String)o);
          }else{
              return o!=null;
          }
        },keys,"can't be blank!");
    }

    /**
     * 正则校验
     * @param params
     * @param regex 正则表达式
     * @param keys 需要验证的key
     */
    public static void matches(Object params,String regex,String... keys){
        valid(params,o->{
            if(o==null) return true;//为空则不校验

            String clazz = o.getClass().getSimpleName();
            String value = "";
            switch (clazz){
                case "Integer":
                case "Long":
                case "String":
                    value = o.toString();break;
                default:
                    ErrorUtil.error("Only numbers or strings can be matched!");
            }
            //为空则不校验，返回true
            if(StringUtils.isBlank(value)) return true;
            return Pattern.matches(regex,value);

        },keys,"mismatch!");
    }

    public static void matches(String value,String regex){
        if(!Pattern.matches(regex,value)){
            ErrorUtil.error(value +"mismatch!");
        }
    }

}
