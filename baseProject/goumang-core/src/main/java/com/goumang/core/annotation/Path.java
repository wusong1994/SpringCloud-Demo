package com.goumang.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将临时路径转换为永久路径的注解，标注在实体{@link com.goumang.core.base.BasePo}的属性上
 * @author hrb
 * @since 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {

    String separator() default "";
}
