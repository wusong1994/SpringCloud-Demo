package com.goumang.sys.mapper;

import com.goumang.core.base.BaseMapper;
import com.goumang.sys.api.po.SysActionPo;
import com.goumang.sys.api.vo.SysActionVo;

import java.util.List;
import java.util.Map;

public interface SysActionMapper extends BaseMapper<SysActionPo> {

    List<SysActionVo> selectByParams(Map<String, Object> map);
}
