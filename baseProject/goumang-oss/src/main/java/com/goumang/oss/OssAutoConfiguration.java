package com.goumang.oss;

import com.goumang.oss.factory.OssTemplateFactory;
import com.goumang.core.template.OssTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Create by huangzy  on 2019/9/4
 */
@Configuration
@ComponentScan
public class OssAutoConfiguration {

    @Autowired
    OssTemplateFactory ossTemplateFactory;

    @Bean
    public OssTemplate ossTemplate() {
        OssTemplate ossTemplate = ossTemplateFactory.getOssTemplate();
        return ossTemplate;
    }
}
