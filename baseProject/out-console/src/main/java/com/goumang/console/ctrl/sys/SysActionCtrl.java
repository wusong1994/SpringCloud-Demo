package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysActionFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/8/26
 */
@RestController
@RequestMapping("/console/sys/action")
public class SysActionCtrl {

    @Autowired
    private SysActionFeign sysActionFeign;

    @GetMapping(value = "{id}")
    public WebResponse info(@PathVariable("id") Long id){
        return sysActionFeign.info(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebResponse list(@RequestBody Map<String,Object> map){
        return sysActionFeign.list(map);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> map){
        return sysActionFeign.page(map);
    }

    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        return sysActionFeign.insert(map);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        return sysActionFeign.update(map);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public WebResponse delete(@PathVariable("id") Long id){
        return sysActionFeign.delete(id);
    }

    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public WebResponse exist(@RequestBody Map<String,Object> map){
        return sysActionFeign.exist(map);
    }

    /** 添加默认的操作点 */
    @RequestMapping(value = "/addDefault/{funcId}", method = RequestMethod.POST)
    public WebResponse addDefault(@PathVariable("funcId") Long funcId){
        return sysActionFeign.addDefault(funcId);
    }

    /** 分页连接角色 */
    @RequestMapping(value = "/pageJoinRole", method = RequestMethod.POST)
    public WebResponse pageJoinRole(@RequestBody Map<String,Object> map){
        return sysActionFeign.pageJoinRole(map);
    }
}
