package com.goumang.console.ctrl.base;

import com.goumang.core.annotation.NoLogin;
import com.goumang.core.template.OssTemplate;
import com.goumang.core.util.OssUtil;
import com.goumang.core.util.SessionUtil;
import com.goumang.core.web.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * By huang.rb on 2019/8/16
 */
@RestController
@RequestMapping("/console/base/oss")
public class OssCtrl {

    @Autowired(required = false)
    private OssTemplate ossTemplate;

    /**
     * 上传(按照系统规则存放)
     * @param file 文件
     * @param persist 是否持久化
     * @return
     * @throws Exception
     */
    @NoLogin
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public WebResponse upload(@RequestPart("file") MultipartFile file,@RequestParam(required = false,value ="persist" ) boolean persist) throws Exception{
        String fileName = file.getOriginalFilename();
        String fileSuffix  = fileName.substring(fileName.lastIndexOf(".")+1).trim();
        return new WebResponse(ossTemplate.upload(file, OssUtil.getStorePath(fileSuffix,persist)));
    }


    /**
     * 批量上传(指定目录上传)
     * @param files 文件
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/multiple/upload/**", method = RequestMethod.POST)
    public WebResponse multipleUpload(@RequestPart("file") MultipartFile[] files) throws Exception{
        String path = SessionUtil.getPath();
        List<String> paths = new ArrayList<>();
        for(MultipartFile file :files){
            String fileName = file.getOriginalFilename();
            paths.add(ossTemplate.upload(file, path + "/" +fileName));
        }
        return new WebResponse(paths);
    }

    /**
     * 下载
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/download/**", method = RequestMethod.GET)
    public WebResponse download(HttpServletRequest request, HttpServletResponse response){
        String path = SessionUtil.getPath();
        return new WebResponse(ossTemplate.download(path, request, response));
    }
    /** 创建目录 */
    @RequestMapping(value = "/mkdir/**", method = RequestMethod.POST)
    public WebResponse mkdir(){
        String path = SessionUtil.getPath();
        return new WebResponse(ossTemplate.mkdir(path));
    }

    /** 重命名 */
    @RequestMapping(value = "/rename/**", method = RequestMethod.POST)
    public WebResponse rename(@RequestParam("name") String name){
        String path = SessionUtil.getPath();
        return new WebResponse(ossTemplate.rename(path, name));
    }

    /** 删除 */
    @RequestMapping(value = "/delete/**", method = RequestMethod.DELETE)
    public WebResponse delete(){
        String path = SessionUtil.getPath();
        return new WebResponse(ossTemplate.delete(path));
    }

    /** 目录列表 */
    @RequestMapping(value = "/dir/list/**", method = RequestMethod.GET)
    public WebResponse dirList(){
        String path = SessionUtil.getPath();
        return new WebResponse(ossTemplate.listDir(path));
    }

    /** 文件列表 */
    @RequestMapping(value = "/file/list/**", method = RequestMethod.GET)
    public WebResponse fileList(@RequestParam(name = "prefix",required = false) String prefix){
        String path = SessionUtil.getPath();
        return new WebResponse(ossTemplate.listFile(path, prefix));
    }

}
