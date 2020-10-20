package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysRoleActionPo;
import com.goumang.sys.service.SysRoleActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sys/roleAction")
public class SysRoleActionCtrl  extends BaseCtrl<SysRoleActionPo> {

    @Autowired
    SysRoleActionService sysRoleActionService;

    @Override
    public SysRoleActionPo preDelete(SysRoleActionPo po) {
        ParamUtil.notBlank(po,"roleId","funcId");
        return super.preDelete(po);
    }

    /** 批量操作 */
    @RequestMapping(value = "/batchOperation", method = RequestMethod.POST)
    public WebResponse batchOperation(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"roleId","funcId");
        return new WebResponse(sysRoleActionService.batchOperation(map));
    }


}
