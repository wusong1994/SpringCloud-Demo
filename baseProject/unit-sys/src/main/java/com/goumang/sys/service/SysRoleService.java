package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.sys.api.po.SysRolePo;

import java.util.List;

public interface SysRoleService extends BaseService<SysRolePo> {

    /**
     * 获取角色的权限
     * @param roleCode 角色编码
     * @return
     */
    List<String> getPermissions(String roleCode);


}
