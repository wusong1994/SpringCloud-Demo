package com.goumang.core.web;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于启用默认错误控制器的注释
 * @author Gene
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackageClasses= BasicControllerAdvice.class)
public @interface EnableControllerAdvice {

}
