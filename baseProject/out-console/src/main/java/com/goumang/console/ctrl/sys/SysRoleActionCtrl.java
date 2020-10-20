package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysRoleActionFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * By huang.rb on 2019/8/30
 */
@RestController
@RequestMapping("/console/sys/roleAction")
public class SysRoleActionCtrl {

    @Autowired
    private SysRoleActionFeign sysRoleActionFeign;

    @RequestMapping(method = RequestMethod.DELETE)
    public WebResponse delete(@RequestBody Map<String,Object> map){
        return sysRoleActionFeign.delete(map);
    }

    /** 批量操作 */
    @RequestMapping(value = "/batchOperation", method = RequestMethod.POST)
    public WebResponse batchOperation(@RequestBody Map<String,Object> map){
        return sysRoleActionFeign.batchOperation(map);
    }
}
