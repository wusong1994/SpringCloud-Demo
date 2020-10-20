package com.goumang.core.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * By huang.rb on 2019/10/31
 */
public abstract class BaseJsonSerializer <T,R> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            JsonStreamContext context = gen.getOutputContext();
            Class<?> clazz = context.getCurrentValue().getClass();
            String name = context.getCurrentName();
            Field field = getField(clazz,name);
            R r = serialize(value,field);
            if(null!=r){
                if(r instanceof String) gen.writeString(r.toString());
                else  gen.writeObject(r);
            }else {
                gen.writeNull();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract R serialize(T value,  Field field);

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
