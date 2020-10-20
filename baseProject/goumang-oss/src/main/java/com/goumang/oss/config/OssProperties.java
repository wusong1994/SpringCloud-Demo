package com.goumang.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create by huangzy  on 2019/9/4
 */

@ConfigurationProperties(prefix = "goumang.oss")
@Component
public class OssProperties {

    //存储类型
    private StorageType type = StorageType.local;

    //存储空间
    private String bucket;

    private Ali ali;

    public StorageType getType() {
        return type;
    }

    public void setType(StorageType type) {
        this.type = type;
    }

    public Ali getAli() {
        return ali;
    }

    public void setAli(Ali ali) {
        this.ali = ali;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
