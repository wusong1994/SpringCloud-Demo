package com.goumang.oss.template.impl;

import com.aliyun.oss.OSSClient;
import com.goumang.core.template.OssFilePo;
import com.goumang.core.template.OssPo;
import com.goumang.core.template.OssTemplate;
import com.goumang.oss.config.Ali;
import com.goumang.oss.config.OssProperties;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Create by huangzy  on 2019/9/4
 */
public class OssAliTemplateImpl implements OssTemplate {

    //存储空间
    private String bucket;

    //图片域名
    private String domain;

    //阿里云oss SDK
    private OSSClient ossClient;


    public OssAliTemplateImpl(OssProperties properties) {
        bucket = properties.getBucket();
        domain = properties.getAli().getDomain();
        Ali ali = properties.getAli();
        try {
            ossClient = new OSSClient(ali.getEndpoint(), ali.getAccessKeyID(), ali.getAccessKeySecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String upload(InputStream is, String dest) {
        ossClient.putObject(bucket, dest, is);
        return domain + "/" + dest;
    }

    @Override
    public String upload(MultipartFile file, String dest) {
        try {
            ossClient.putObject(bucket, dest, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return domain + "/" + dest;
    }

    @Override
    public int download(String path, HttpServletRequest request, HttpServletResponse response) {
        //TODO
        return 0;
    }

    @Override
    public void move(String src, String dest) {
        if (src.equals(dest)) return;
        ossClient.copyObject(bucket, src.replaceAll(domain + "/", ""), bucket, dest.replaceAll(domain + "/", ""));
        delete(src);
    }

    @Override
    public boolean delete(String src) {
        try{
            ossClient.deleteObject(bucket, src.replaceAll(domain + "/", ""));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rename(String path, String name) {
        //TODO
        return false;
    }

    @Override
    public boolean exist(String src) {
        return ossClient.doesObjectExist(bucket, src.replaceAll(domain + "/", ""));
    }

    @Override
    public List<OssPo> listDir(String path) {
        //TODO
        return null;
    }

    @Override
    public List<OssFilePo> listFile(String path, String prefix) {
        //TODO
        return null;
    }

    @Override
    public boolean mkdir(String path) {
        //TODO
        return false;
    }

}
