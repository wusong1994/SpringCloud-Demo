package com.goumang.sys.api.vo;

import com.goumang.sys.api.po.SysActionPo;

/**
 * By huang.rb on 2019/8/30
 */
public class SysActionVo extends SysActionPo {

    /* 角色操作点ID */
    private Long roacId;

    /* 角色ID */
    private Long roleId;

    public Long getRoacId() {
        return roacId;
    }

    public void setRoacId(Long roacId) {
        this.roacId = roacId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
