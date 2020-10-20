package com.goumang.oss.config;

/**
 * Create by huangzy  on 2019/9/5
 */
public class Ali {


    private String accessKeyID;

    private String accessKeySecret;

    private String endpoint;

    //阿里云图片域名
    private String domain;

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public void setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
