package com.goumang.core.util;

import com.goumang.core.base.BasePo;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * By huang.rb on 2018/12/10
 */
public class MapperUtil {

    public static <T extends BasePo> void setOrderBy(T t, Weekend wk){
        if(StringUtils.isBlank(t.getOrderBy())) return;
        wk.setOrderByClause(t.getOrderBy());
    }

    public static <T extends BasePo> void setAndEqual(T t, WeekendCriteria<T,Object> criteria){
        setAndEqual(t,criteria,null,false);
    }
    public static <T extends BasePo> void setAndEqual(T t, WeekendCriteria<T,Object> criteria, boolean clean){
        setAndEqual(t,criteria,null,clean);
    }
    public static <T extends BasePo> void setAndEqual(T t, WeekendCriteria<T,Object> criteria, String... ingoreFileds){
        setAndEqual(t,criteria,ingoreFileds,false);
    }

    /**
     * 设置等于查询条件
     * @param t 实体
     * @param criteria 条件
     * @param ingoreFileds 忽略的字段
     * @param clean 是否清除已有条件的字段 true-清除，false不清除
     * @param <T>
     */
    public static <T extends BasePo> void setAndEqual(T t, WeekendCriteria<T,Object> criteria, String[] ingoreFileds, boolean clean){
        try {
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                //过滤
                if(field.getAnnotation(Transient.class)!=null) continue;
                String name = field.getName();
                //忽略的字段
                if( ingoreFileds!=null && ingoreFileds.length >0 && Arrays.asList(ingoreFileds).contains(name)) continue;
                if(clean && cleanField(field,criteria)) continue;
                String methodName = "get"+name.substring(0,1).toUpperCase()+name.substring(1);
                Method method = clazz.getDeclaredMethod(methodName);
                Object value = method.invoke(t);
                if(value==null) continue;
                if(value instanceof String){
                    if(((String) value).trim().isEmpty()) continue;
                }

                //条件设置
                criteria.andEqualTo(field.getName(),value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtil.error(e.getMessage());
        }
    }

    public static <T extends BasePo> void andLike(T t,WeekendCriteria<T,Object> criteria,String... fields){
        String methodName = null;
        try{
            Class clazz = t.getClass();
            for(String field: fields){

                methodName = "get"+field.substring(0,1).toUpperCase()+field.substring(1);
                Method method = clazz.getDeclaredMethod(methodName);
                Object value = method.invoke(t);
                if(value==null || !(value instanceof String) || value.toString().trim().length() == 0 ) continue;
                criteria.andLike(field, "%" + value.toString()+ "%");

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtil.error(e.getMessage());
        }

    }



    private static <T extends BasePo> boolean cleanField(Field field, WeekendCriteria<T,Object> criteria){
        List<Example.Criterion> list = criteria.getCriteria();
        String fieldName = field.getName();
        for(Example.Criterion c:list){
            String cond = c.getCondition();
            String name = cond.substring(0,cond.indexOf(" ")==-1?cond.length():cond.indexOf(" "));
            if(fieldName.equals(name)) return  true;
        }
        return false;
    }

    //驼峰转下划线
    private static String humpToUnderline(String property){
        StringBuilder sb=new StringBuilder(property);
        int temp=0;//定位
        for(int i=0;i<property.length();i++){
            if(Character.isUpperCase(property.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }

    private static <T extends BasePo> boolean isOrderBy(T t,String orderBy){
        if(StringUtils.isBlank(orderBy)) return false;
        String[] arr = orderBy.split(" ");
        String field = arr[0];
        String sort = "asc";
        if(arr.length >= 2) sort = arr[1].trim();
        String regex = "";//desc asc

        Pattern.matches(regex,orderBy);
        return false;
    }

}
