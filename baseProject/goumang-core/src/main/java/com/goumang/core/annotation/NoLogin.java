package com.goumang.core.annotation;

import java.lang.annotation.*;

/**
 * 标注了该注解的被都不会被登录拦截器{@link com.goumang.core.interceptor.LoginInterceptor}拦截
 * @author hrb
 * @since 1.0
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLogin {
}
