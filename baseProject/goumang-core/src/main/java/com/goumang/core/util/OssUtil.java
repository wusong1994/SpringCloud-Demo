package com.goumang.core.util;

import cn.hutool.core.date.DateUtil;
import com.goumang.core.emun.PathEnum;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.Date;
import java.util.UUID;

/**
 * By huang.rb on 2019/9/20
 */
public class OssUtil {



    public static String getStorePath(String suffix){
        Object id = SessionUtil.getUserId();
        return getStorePath(suffix,id.toString(),false);
    }

    /**
     * 获取文件的存储路径
     * @param suffix 文件后缀
     * @param persist 是否持久化
     * @return
     */
    public static String getStorePath(String suffix,boolean persist){
        Object id = SessionUtil.getUserId();
        return getStorePath(suffix,id.toString(),persist);
    }

    public static String getStorePath(String suffix,String userId){
        return getStorePath(suffix,userId,false);
    }

    /**
     * 获取文件的存储路径
     * @param suffix 文件后缀
     * @param persist 是否持久化
     * @return
     */
    public static String getStorePath(String suffix,String userId,boolean persist){
        String root = persist ? PathEnum.PERM.getPath() : PathEnum.TEMP.getPath();
        String path =  root + "/" + (userId == null ? PathEnum.VISITOR.getPath() :  userId);
        path += "/" + DateUtil.format(new Date(),"yyyyMM");

        String name = UUID.randomUUID().toString().replace("-","")+"."+suffix;

        return path + "/" + name;
    }

    /**
     * 对上传文件的配置
     * @param registry ResourceHandlerRegistry
     * @param localDir 本地存储目录
     */
    public static void registryResourceHandler(ResourceHandlerRegistry registry, String localDir){
        registry.addResourceHandler("/"+PathEnum.TEMP.getPath() +"/**","/"+PathEnum.PERM.getPath() +"/**")
                .addResourceLocations("file:" + localDir +"/"+ "/"+PathEnum.TEMP.getPath() +"/","file:" + localDir +"/"+PathEnum.PERM.getPath() +"/");
    }


}
