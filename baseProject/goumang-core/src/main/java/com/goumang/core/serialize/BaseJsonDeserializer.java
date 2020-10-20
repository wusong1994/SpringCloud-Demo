package com.goumang.core.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * By huang.rb on 2019/10/31
 */
public abstract class BaseJsonDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Class<?> clazz = p.getCurrentValue().getClass();
        String name = p.getCurrentName();
        Field field = getField(clazz,name);
        String value = p.getText();
        T t = deserializer(value,field);

        return t;
    }

    public abstract T deserializer(String value,  Field field);

    private Field getField(Class<?> clazz,String name){
        Field field=null;
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(name) ;
                break;
            } catch (NoSuchFieldException e) {

            }
        }
        return field;
    }
}
