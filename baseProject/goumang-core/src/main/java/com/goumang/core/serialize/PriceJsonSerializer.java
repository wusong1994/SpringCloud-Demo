package com.goumang.core.serialize;

import com.goumang.core.annotation.JsonPrice;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * By huang.rb on 2019/10/31
 */
public class PriceJsonSerializer extends BaseJsonSerializer<String,String>{

    @Override
    public String serialize(String value, Field field) {
        JsonPrice jsonPrice = field.getAnnotation(JsonPrice.class);
        String currency = jsonPrice.value().toUpperCase();
        BigDecimal input = new BigDecimal(value);
        String output = null;
        switch (currency){
            case "CNY":
            case "RMB":
                input = input.movePointLeft(2);
                output = String.format("%.2f", input);
                break;
        }
        return output;
    }
}
