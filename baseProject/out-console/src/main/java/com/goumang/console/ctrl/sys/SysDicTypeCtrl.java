package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysDicTypeFeign;
import com.goumang.sys.api.po.SysDicTypePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/8/20
 */
@RestController
@RequestMapping("/console/sys/dicType")
public class SysDicTypeCtrl {

    @Autowired
    private SysDicTypeFeign sysDicTypeFeign;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebResponse info(@PathVariable("id") Object id){
        return  sysDicTypeFeign.info(id);
    }

    /** 是否存在 */
    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public WebResponse exist(@RequestBody Map<String, Object> map){
        return sysDicTypeFeign.exist(map);
    }

    /** 分页 */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> map){
        return  sysDicTypeFeign.page(map);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebResponse list(@RequestBody Map<String,Object> map){
        return sysDicTypeFeign.list(map);
    }

    /** 插入 */
    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        return sysDicTypeFeign.insert(map);
    }

    /** 更新 */
    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        return sysDicTypeFeign.update(map);
    }

    /** 删除 */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public WebResponse delete(@PathVariable("id") Object id){
        return sysDicTypeFeign.delete(id);
    }
}
