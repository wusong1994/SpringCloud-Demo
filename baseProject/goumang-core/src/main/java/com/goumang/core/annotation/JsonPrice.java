package com.goumang.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.goumang.core.serialize.PriceJsonDeserializer;
import com.goumang.core.serialize.PriceJsonSerializer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * @author hrb
 * @since 1.0
 */
@Documented
@Target(java.lang.annotation.ElementType.FIELD )
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = PriceJsonSerializer.class)
@JsonDeserialize(using = PriceJsonDeserializer.class)
public @interface JsonPrice {

    String value() default "CNY";
}
