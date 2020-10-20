package com.goumang.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * By huang.rb on 2019/3/18
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SysApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class, args);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SysApplication.class);
    }
}
