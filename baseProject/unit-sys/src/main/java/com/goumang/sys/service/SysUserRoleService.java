package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.sys.api.po.SysUserRolePo;

import java.util.Map;
import java.util.Set;

public interface SysUserRoleService extends BaseService<SysUserRolePo> {

    /**
     * 批量操作
     * @param map [roleId-角色Id；userIds-用户Id，多个以逗号隔开]
     * @return
     */
    int batchOperation(Map<String, Object> map);

    /**
     * 获取用户权限
     * @param userId 用户权限
     * @return
     */
    Map<String, Set<String>> permissions(Long userId);

    /**
     * 保存
     * @param userId 用户Id
     * @param roleIds 角色Id
     */
    void batchSave(Long userId,String[] roleIds);
}
