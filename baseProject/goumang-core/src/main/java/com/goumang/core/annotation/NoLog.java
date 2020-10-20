package com.goumang.core.annotation;

import java.lang.annotation.*;

/**
 * 标注了该注解的都不会被日志拦截器{@link com.goumang.core.interceptor.LogInterceptor}拦截
 * @author hrb
 * @since 1.0
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLog {
}
