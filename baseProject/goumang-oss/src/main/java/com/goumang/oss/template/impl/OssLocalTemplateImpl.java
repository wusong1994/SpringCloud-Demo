package com.goumang.oss.template.impl;

import cn.hutool.core.io.FileUtil;
import com.goumang.core.template.OssFilePo;
import com.goumang.core.template.OssPo;
import com.goumang.core.template.OssTemplate;
import com.goumang.core.util.ErrorUtil;
import com.goumang.oss.config.OssProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by huangzy  on 2019/9/4
 */
public class OssLocalTemplateImpl implements OssTemplate {

    /**
     * 存储空间
     */
    private String bucket;

    public OssLocalTemplateImpl(OssProperties properties) {
        bucket = properties.getBucket();
    }

    @Override
    public String upload(InputStream is, String dest) {
        FileUtil.writeFromStream(is,bucket + "/" + dest);
        return "/" + dest;
    }

    @Override
    public String upload(MultipartFile file, String dest) {
        String absolutePath = bucket + "/" + dest;
        try {
            mkdirDirectory(dest);
            file.transferTo(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(dest.trim().startsWith("/")) return dest.trim();
        else return "/" + dest.trim();
    }

    @Override
    public int download(String path, HttpServletRequest request, HttpServletResponse response) {
        try{
            if(path.startsWith("/")) path = path.substring(1);

            File file = new File(bucket + "/" + path);
            if(file.isDirectory()) ErrorUtil.error("不是文件");
            FileInputStream fis = new FileInputStream(file);
            String fileName = path.substring(path.lastIndexOf("/")+1);
            //获取文件后缀（.txt）
            String extendFileName = path.substring(path.lastIndexOf("."));
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
            ServletOutputStream os = response.getOutputStream();

            return FileCopyUtils.copy(fis,os);
        }catch (Exception e){
            e.printStackTrace();
            ErrorUtil.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public void move(String src, String dest) {
        if (src.equals(dest)) return;
        File file = new File(bucket + "/" + src);
        if (!file.exists() || file.isDirectory()) return;
        mkdirDirectory(dest);
        file.renameTo(new File(bucket + "/" + dest));
        file.delete();
    }

    @Override
    public boolean delete(String src) {
        File file = new File(bucket + "/" + src);
        return file.delete();
    }

    @Override
    public boolean rename(String path, String name) {
        if(path.startsWith("/")) path = path.substring(1);
        File file = new File(bucket +"/"+path);
        int i = path.lastIndexOf("/");
        name = i==-1?name: path.substring(0,i) +"/"+ name;
        return file.renameTo(new File(bucket +"/"+name));
    }

    @Override
    public boolean exist(String src) {
        File file = new File(bucket + "/" + src);
        return file.exists();
    }

    @Override
    public List<OssPo> listDir(String path) {
        if(StringUtils.isBlank(path)) path = "/";
        String dirPath = bucket + (path.startsWith("/")?path:"/"+path);
        File dir = new File(dirPath);
        if(!dir.isDirectory()) ErrorUtil.error(path +"不是目录");
        File[] dirs = dir.listFiles(o->o.isDirectory());
        List<OssPo> res = Arrays.stream(dirs).map(o->{
            OssPo po = new OssPo();
            po.setName(o.getName());
            po.setPath(o.getPath().replace("\\","/").replaceFirst(bucket,""));
            po.setLastModified(new Date(o.lastModified()));
            po.setDirectory(true);
            return po;
        }).collect(Collectors.toList());
        return res;
    }

    @Override
    public List<OssFilePo> listFile(String path, String prefix) {

        if(StringUtils.isBlank(path)) path = "/";
        String dirPath = bucket + (path.startsWith("/")?path:"/"+path);
        File dir = new File(dirPath);
        if(!dir.isDirectory()) ErrorUtil.error(path +"不是目录");
        File[] files = dir.listFiles(o->{
            if(StringUtils.isBlank(prefix)){
                return !o.isDirectory();
            }else{
                return !o.isDirectory() && o.getName().startsWith(prefix);
            }
        });
        List<OssFilePo> res = Arrays.stream(files).map(o->{
            OssFilePo po = new OssFilePo();
            po.setName(o.getName());

            po.setPath(o.getPath().replace("\\","/").replaceFirst(bucket,""));
            po.setLastModified(new Date(o.lastModified()));
            Path source = Paths.get(o.getPath());
            String contentType = null;
            try {
                contentType = Files.probeContentType(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            po.setContentType(contentType);
            po.setSize(o.length());
            po.setDirectory(false);
            po.setLeaf(true);
            return po;
        }).collect(Collectors.toList());
        return res;
    }

    @Override
    public boolean mkdir(String path) {
        if(path.startsWith("/")) path = path.substring(1);
        File file  = new File(bucket +"/" +path);
        return file.mkdir();
    }

    /**
     * 创建地址上的文件夹(存在则不创建)
     * @param dest
     */
    private void mkdirDirectory(String dest) {

        int i = dest.lastIndexOf("/");
        if (i == -1) return;
        String directory = dest.substring(0, (i + 1));
        File file = new File(bucket + "/" + directory);

        if (file.exists() && file.isDirectory()) return;
        file.mkdirs();
    }

}
