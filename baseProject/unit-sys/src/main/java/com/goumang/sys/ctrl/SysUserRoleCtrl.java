package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysUserRolePo;
import com.goumang.sys.service.SysUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/sys/userRole")
public class SysUserRoleCtrl  extends BaseCtrl<SysUserRolePo> {

    @Autowired
    SysUserRoleService sysUserRoleService;

    /** 批量操作 */
    @RequestMapping(value = "/batchOperation", method = RequestMethod.POST)
    public WebResponse batchOperation(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"roleId");
        int i = sysUserRoleService.batchOperation(map);
        return new WebResponse(i);
    }

    /** 获取用户权限 */
    @RequestMapping(value = "/permission/{userId}", method = RequestMethod.GET)
    public WebResponse permission(@PathVariable("userId") Long userId){
        Map<String, Set<String>> permissions = sysUserRoleService.permissions(userId);
        return new WebResponse(permissions);
    }

    /** 批量保存 */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public WebResponse batchSave(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"userId");
        Long userId = Long.parseLong(map.get("userId").toString());
        String roleIds = (String) map.get("roleIds");

        String[] roleId_arr = StringUtils.isBlank(roleIds) ? new String[0]:roleIds.split(",");
        sysUserRoleService.batchSave(userId,roleId_arr);
        return new WebResponse();
    }
}
