package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysRoleFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/8/30
 */
@RestController
@RequestMapping("/console/sys/role")
public class SysRoleCtrl {

    @Autowired
    private SysRoleFeign sysRoleFeign;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebResponse info(@PathVariable("id") Long id){
        return sysRoleFeign.info(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebResponse list(@RequestBody Map<String,Object> map){
        return sysRoleFeign.list(map);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> map){
        return sysRoleFeign.page(map);
    }

    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        return sysRoleFeign.insert(map);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        return sysRoleFeign.update(map);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public WebResponse delete(@PathVariable("id") Long id){
        return sysRoleFeign.delete(id);
    }

    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public WebResponse exist(@RequestBody Map<String,Object> map){
        return sysRoleFeign.exist(map);
    }


}
