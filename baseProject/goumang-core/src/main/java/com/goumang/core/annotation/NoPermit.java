package com.goumang.core.annotation;

import java.lang.annotation.*;

/**
 * 标注了该注解的被都不会被权限拦截器{@link com.goumang.core.interceptor.PermissionInterceptor}拦截
 * @author hrb
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoPermit {

}
