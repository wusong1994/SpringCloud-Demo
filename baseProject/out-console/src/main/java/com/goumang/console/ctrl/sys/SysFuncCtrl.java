package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysFuncFeign;
import com.goumang.sys.api.util.SysFuncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * By huang.rb on 2019/8/26
 */
@RestController
@RequestMapping("/console/sys/func")
public class SysFuncCtrl {

    @Autowired
    private SysFuncFeign sysFuncFeign;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebResponse info(@PathVariable("id") Long id){
        return sysFuncFeign.info(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebResponse list(@RequestBody Map<String,Object> map){
        return sysFuncFeign.list(map);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> map){
        return sysFuncFeign.page(map);
    }

    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        return sysFuncFeign.insert(map);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        return sysFuncFeign.update(map);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public WebResponse delete(@PathVariable("id") Long id){
        return sysFuncFeign.delete(id);
    }

    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public WebResponse exist(@RequestBody Map<String,Object> map){
        return sysFuncFeign.exist(map);
    }

    /** 分页连接角色 */
    @RequestMapping(value = "/pageJoinRole", method = RequestMethod.POST)
    public WebResponse pageJoin(@RequestBody Map<String,Object> map){
        return sysFuncFeign.pageJoin(map);
    }

    /** 生成功能点 */
    @RequestMapping(value = "/saveAllApi、", method = RequestMethod.POST)
    public WebResponse getAllApi(){
        Map<RequestMappingInfo, HandlerMethod> methodsMap = handlerMapping.getHandlerMethods();
        Map<String,List<String[]>> funcMap = SysFuncUtil.getFuncMap(methodsMap,"console");
        return new WebResponse(sysFuncFeign.saveAllApi(funcMap));
    }

}
