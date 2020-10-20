package com.goumang.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * By huang.rb on 2019/3/18
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.goumang.**.api.feign"})
@ServletComponentScan
public class ConsoleApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ConsoleApplication.class);
    }

}
