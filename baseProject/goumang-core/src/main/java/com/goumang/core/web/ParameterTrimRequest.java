package com.goumang.core.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParameterTrimRequest extends HttpServletRequestWrapper {

    private Map<String , String[]> params = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    public ParameterTrimRequest(HttpServletRequest request) {
        super(request);
        Map<String, String[]> requestMap = request.getParameterMap();
        this.params.putAll(requestMap);
        this.modifyParameterValues();
    }

    /**
     * 重写getInputStream方法  post类型的请求参数必须通过流才能获取到值
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        //非json类型，直接返回
        if(!super.getHeader(HttpHeaders.CONTENT_TYPE).toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE)){
            return super.getInputStream();
        }
        //为空，补上括号
        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isEmpty(json)) {
            json = "{}";
            ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            return new MyServletInputStream(bis);
        }
        Map<String,Object> map= objectMapper.readValue(json,Map.class);
        Map<String,Object> newMap=new HashMap<>();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            if(!StringUtils.isEmpty(entry.getValue()))
                newMap.put( entry.getKey(),entry.getValue());
        }
        String newJson = objectMapper.writeValueAsString(newMap);

        ByteArrayInputStream bis = new ByteArrayInputStream(newJson.getBytes(StandardCharsets.UTF_8));
        return new MyServletInputStream(bis);
    }
    /**
     * 将parameter的值去除空格后重写回去
     */
    private void modifyParameterValues(){
        Set<String> set =params.keySet();
        for (String key : set) {
            String[] values = params.get(key);
            values[0] = values[0].trim();
            params.put(key, values);
        }
    }
    /**
     * 重写getParameter 参数从当前类中的map获取
     */
    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }
    /**
     * 重写getParameterValues
     */
    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }

    class MyServletInputStream extends  ServletInputStream{
        private ByteArrayInputStream bis;
        MyServletInputStream(ByteArrayInputStream bis){
            this.bis=bis;
        }
        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }
        @Override
        public int read() {
            return bis.read();
        }
    }

}
