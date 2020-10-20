package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.sys.api.po.SysRoleActionPo;

import java.util.Map;

public interface SysRoleActionService extends BaseService<SysRoleActionPo> {

    int batchInsert(Map<String, Object> map);

    /**
     * 批量操作
     * @param map [roleId-角色ID,funcId-功能点ID,actionIds-操作点ID，多个以逗号隔开]
     * @return
     */
    int batchOperation(Map<String, Object> map);
}
