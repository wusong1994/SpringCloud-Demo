package com.goumang.console;


import com.goumang.core.interceptor.LoginInterceptor;
import com.goumang.core.interceptor.PermissionInterceptor;
import com.goumang.core.util.OssUtil;
import com.goumang.core.web.EnableControllerAdvice;
import com.goumang.oss.config.OssProperties;
import com.goumang.oss.config.StorageType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * By huang.rb on 2019/3/18
 */
@Configuration
@EnableControllerAdvice
@EnableRedisHttpSession
public class ConsoleConfiguration implements WebMvcConfigurer {

    @Autowired
    private OssProperties properties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/console/**");   //登录拦截器
        registry.addInterceptor(permissionInterceptor()).addPathPatterns("/console/**"); //权限拦截器
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //本地上传文件访问
        if(properties!=null && StringUtils.isNotBlank(properties.getBucket()) && properties.getType().equals(StorageType.local)){
            OssUtil.registryResourceHandler(registry,properties.getBucket());
        }
    }

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor(true); //设登录拦截器默认拦截所有方法
    }

    @Bean
    public PermissionInterceptor permissionInterceptor(){
        return new PermissionInterceptor();
    }


}
