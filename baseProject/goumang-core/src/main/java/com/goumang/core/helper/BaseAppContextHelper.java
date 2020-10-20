package com.goumang.core.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author hrb
 * @param
 * @since 1.0
 */
public class BaseAppContextHelper implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public BaseAppContextHelper() {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BaseAppContextHelper.ctx = context;
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return ctx.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }
}
