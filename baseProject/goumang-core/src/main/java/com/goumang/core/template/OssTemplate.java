package com.goumang.core.template;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * Create by huangzy  on 2019/9/4
 */

public interface OssTemplate{


    /**
     * 上传文件
     * @param is
     * @param dest
     * @return
     */
    String upload(InputStream is, String dest);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param dest 文件存放地址
     */
    String upload(MultipartFile file, String dest);

    /**
     * 文件下载
     * @param path
     * @param request
     * @param response
     */
    int download(String path, HttpServletRequest request, HttpServletResponse response);

    /**
     * 移动文件位置
     *
     * @param src 源文件地址
     * @param dest 目标地址
     */
    void move(String src, String dest);

    /**
     * 删除文件
     *
     * @param src 源文件地址
     */
    boolean delete(String src);

    /**
     * 重命名
     * @param path
     * @param name
     * @return
     */
    boolean rename(String path, String name);

    /**
     * 文件是否存在
     * @param src 源文件地址
     * @return true-存在，false-不存在
     */
    boolean exist(String src);

    /**
     * 列出目录列表
     * @param path，相对路径，为空则返回存储空间下的目录
     * @return
     */
    List<OssPo> listDir(String path);

    /**
     * 列出目录的文件
     * @param path 相对路径，为空则返回存储空间下的文件
     * @param prefix 文件前缀
     * @return
     */
    List<OssFilePo> listFile(String path,String prefix);

    /**
     * 创建目录
     * @param path
     * @return
     */
    boolean mkdir(String path);

}
