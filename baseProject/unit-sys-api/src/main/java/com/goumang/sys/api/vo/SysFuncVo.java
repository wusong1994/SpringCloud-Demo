package com.goumang.sys.api.vo;

import com.goumang.sys.api.po.SysFuncPo;

/**
 * By huang.rb on 2019/8/30
 */
public class SysFuncVo extends SysFuncPo {

    /* 角色功能ID  */
    private Long rofuId;

    /* 角色ID */
    private Long roleId;

    public Long getRofuId() {
        return rofuId;
    }

    public void setRofuId(Long rofuId) {
        this.rofuId = rofuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
