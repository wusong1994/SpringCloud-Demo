package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.sys.api.po.SysRoleFuncPo;

import java.util.Map;

public interface SysRoleFuncService extends BaseService<SysRoleFuncPo> {

    /**
     * 批量保存，并删掉原来的角色功能点及操作点
     * @param map
     * @return
     */
    int batchOperation(Map<String, Object> map);

}
