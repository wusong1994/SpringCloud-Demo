package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysUserRoleFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/9/3
 */
@RestController
@RequestMapping("/console/sys/userRole")
public class SysUserRoleCtrl {

    @Autowired
    private SysUserRoleFeign sysUserRoleFeign;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public WebResponse list(@RequestBody Map<String,Object> map){
        return sysUserRoleFeign.list(map);
    }

    /** 批量操作 */
    @RequestMapping(value = "/batchOperation", method = RequestMethod.POST)
    public WebResponse batchOperation(@RequestBody Map<String,Object> map){
        return sysUserRoleFeign.batchOperation(map);
    }

}
