package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysRoleFuncPo;
import com.goumang.sys.service.SysRoleFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sys/roleFunc")
public class SysRoleFuncCtrl  extends BaseCtrl<SysRoleFuncPo> {

    @Autowired
    SysRoleFuncService sysRoleFuncService;

    /** 批量操作 */
    @RequestMapping(value = "/batchOperation", method = RequestMethod.POST)
    public WebResponse batchOperation(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"roleId");
        return new WebResponse(sysRoleFuncService.batchOperation(map));
    }


}
