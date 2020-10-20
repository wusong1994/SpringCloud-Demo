package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.core.base.Pager;
import com.goumang.sys.api.po.SysActionPo;
import com.goumang.sys.api.vo.SysActionVo;

import java.util.List;
import java.util.Map;

public interface SysActionService extends BaseService<SysActionPo> {

    /**
     * 添加默认的操作点
     * @param funcId 功能点ID
     * @return 插入的记录数
     */
    int addDefault(Long funcId);

    List<SysActionVo> selectByParams(Map<String, Object> map);

    Pager<SysActionVo> selectForPage(Map<String, Object> map);
}
