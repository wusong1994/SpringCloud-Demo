package com.goumang.core.util;



import com.goumang.core.base.BasePo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * By huang.rb on 2019/4/12
 */
public class PojoUtil {

    /**
     * 交换俩个实体的值
     * @param p1
     * @param p2
     * @param fields
     * @param <F>
     */
    public static <F extends BasePo> void exchange(F p1, F p2, String... fields){
        try{
            Class clazz = p1.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for(String field: fields){
                PropertyDescriptor pd =  Arrays.stream(propertyDescriptors).filter(o->field.equals(o.getName())).findFirst().get();
                Method getMethod = pd.getReadMethod();
                Method setMethod = pd.getWriteMethod();
                Object tempValue = getMethod.invoke(p1);
                setMethod.invoke(p1,getMethod.invoke(p2));
                setMethod.invoke(p2,tempValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtil.error(e.getMessage());
        }
    }

    /**
     * 获取俩个集合的交集，并清除左右集合的交集
     * @param left 左集合
     * @param right 右集合
     * @param condition 条件
     * @param <F>
     * @return 交集
     */
    public static <F extends BasePo> List<F> intersect(List<F> left, List<F> right, BiPredicate<F,F> condition){

        List<F> midList = new ArrayList<>();
        Iterator<F> leftIt = left.iterator();
        while (leftIt.hasNext()){
            F l = leftIt.next();
            boolean flag = false; //过滤标记
            Iterator<F> rightIt = right.iterator();
            while (rightIt.hasNext()) {
                F r = rightIt.next();
                flag = condition.test(l,r); //如果相等，则过滤
                if (flag) {
                    l.setPk(r.getPk());
                    rightIt.remove();
                    leftIt.remove();
                    midList.add(r);
                    break;
                }
            }
        }
        return midList;
    }

}
