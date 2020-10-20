package com.goumang.sys.mapper;

import com.goumang.core.base.BaseMapper;
import com.goumang.sys.api.po.SysFuncPo;
import com.goumang.sys.api.vo.SysFuncVo;

import java.util.List;
import java.util.Map;

public interface SysFuncMapper extends BaseMapper<SysFuncPo> {

    List<SysFuncVo> selectByParams(Map<String, Object> map);
}
