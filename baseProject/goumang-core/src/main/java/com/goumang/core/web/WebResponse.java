package com.goumang.core.web;

import cn.hutool.core.bean.BeanUtil;
import com.goumang.core.base.Pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * By huang.rb on 2019/8/7
 */
public class WebResponse{

    private String code = "success";

    private String message = "";

    private Object data;

    public WebResponse() {
    }

    public WebResponse(Object data) {
        this.data = data;
    }

    public WebResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public WebResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public <T> T getData(Class<T> clazz) {
        if(data==null) return null;
        if(!Map.class.isAssignableFrom(data.getClass())) return null;

        Map map = (Map)getData();
        return BeanUtil.mapToBean(map, clazz,true);
    }

    public <T> List<T> getListData(Class<T> clazz){
        if(!List.class.isAssignableFrom(data.getClass())) return null;

        List<Map<String,Object>> list = (List) getData();
        List<T> newList = new ArrayList<>();
        for(Map<String,Object> map : list){
            newList.add(BeanUtil.mapToBean(map, clazz,true));
        }
        return newList;
    }

    public <T> Pager<T> getPagerData(Class<T> clazz){
        if(!Map.class.isAssignableFrom(data.getClass())) return null;

        Map map = (Map)getData();
        Pager pager = BeanUtil.mapToBean(map,Pager.class,true);
        List<Map<String,Object>> list = pager.getContent();
        List<T> newList = new ArrayList<>();
        for(Map<String,Object> poMap : list){
            newList.add(BeanUtil.mapToBean(poMap, clazz,true));
        }
        pager.setContent(newList);

        return pager;
    }

}
