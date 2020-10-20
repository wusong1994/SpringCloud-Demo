package com.goumang.console.ctrl.sys;

import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysRoleFuncFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * By huang.rb on 2019/8/30
 */
@RestController
@RequestMapping("/console/sys/roleFunc")
public class SysRoleFuncCtrl {

    @Autowired
    private SysRoleFuncFeign sysRoleFuncFeign;

    /** 批量操作 */
    @RequestMapping(value = "/batchOperation", method = RequestMethod.POST)
    public WebResponse batchOperation(@RequestBody Map<String,Object> map){
        return sysRoleFuncFeign.batchOperation(map);
    }

}
