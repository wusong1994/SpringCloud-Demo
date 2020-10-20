package com.goumang.sys.api.vo;

import com.goumang.sys.api.po.SysUserPo;

/**
 * By huang.rb on 2019/9/3
 */
public class SysUserVo extends SysUserPo {

    /** 用户角色ID */
    private Long usroId;

    /** 角色ID */
    private Long roleId;

    public Long getUsroId() {
        return usroId;
    }

    public void setUsroId(Long usroId) {
        this.usroId = usroId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
