package com.goumang.console.ctrl.sys;

import com.goumang.core.annotation.NoLogin;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysDicFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/8/20
 */
@RestController
@RequestMapping("/console/sys/dic")
public class SysDicCtrl {

    @Autowired
    private SysDicFeign sysDicFeign;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebResponse info(@PathVariable("id") Long id){
        return sysDicFeign.info(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebResponse list(@RequestBody Map<String,Object> map){
        return sysDicFeign.list(map);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> map){
        return sysDicFeign.page(map);
    }

    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        return sysDicFeign.insert(map);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        return sysDicFeign.update(map);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public WebResponse delete(@PathVariable("id") Long id){
        return sysDicFeign.delete(id);
    }

    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public WebResponse exist(@RequestBody Map<String, Object> map){
        return sysDicFeign.exist(map);
    }

    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public WebResponse sort(@RequestBody Map<String, Object> map) throws Exception {
        return sysDicFeign.sort(map);
    }

    @NoLogin
    @RequestMapping(value = "/cache", method = RequestMethod.GET)
    public WebResponse cache(){
        return sysDicFeign.cache();
    }
}
