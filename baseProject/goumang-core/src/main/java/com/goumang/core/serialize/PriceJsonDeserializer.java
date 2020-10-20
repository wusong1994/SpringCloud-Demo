package com.goumang.core.serialize;

import com.goumang.core.annotation.JsonPrice;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * By huang.rb on 2019/10/31
 */
public class PriceJsonDeserializer extends BaseJsonDeserializer<String> {

    @Override
    public String deserializer(String value, Field field) {
        JsonPrice jsonPrice = field.getAnnotation(JsonPrice.class);
        String currency = jsonPrice.value().toUpperCase();
        String output = null;
        BigDecimal input = new BigDecimal(value);
        switch (currency){
            case "CNY":
            case "RMB":
                input = input.movePointRight(2);
                output = input.toString();
                break;
        }
        return output;
    }
}
