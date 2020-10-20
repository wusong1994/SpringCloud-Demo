package com.goumang.console.ctrl.sys;

import com.goumang.core.annotation.NoPermit;
import com.goumang.core.util.SessionUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysMenuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/9/3
 */
@RestController
@RequestMapping("/console/sys/menu")
public class SysMenuCtrl {

    @Autowired
    private SysMenuFeign sysMenuFeign;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebResponse info(@PathVariable("id") Long id){
        return sysMenuFeign.info(id);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> map){
        return sysMenuFeign.page(map);
    }

    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        return sysMenuFeign.insert(map);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        return sysMenuFeign.update(map);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public WebResponse delete(@PathVariable("id") Long id){
        return sysMenuFeign.delete(id);
    }

    /** 树形查询 */
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public WebResponse tree(@RequestBody Map<String,Object> map){
        return sysMenuFeign.tree(map);
    }

    /** 树形删除 */
    @RequestMapping(value = "/deleteTree/{menuId}", method = RequestMethod.DELETE)
    public WebResponse deleteTree(@PathVariable("menuId") Long menuId){
        return sysMenuFeign.deleteTree(menuId);
    }

    /** 查询用户菜单 */
    @NoPermit
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public WebResponse userMenu(){
        Long userId = SessionUtil.getUserId(Long.class);
        String loginName = SessionUtil.getLoginName("loginName");
        return sysMenuFeign.userMenu("admin".equals(loginName) ? 0 : userId);
    }

    /** 排序 */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public WebResponse sort(@RequestBody Map<String,Object> map){
        return sysMenuFeign.sort(map);
    }
}
