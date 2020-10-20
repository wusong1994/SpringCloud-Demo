package com.goumang.sys.mapper;

import com.goumang.core.base.BaseMapper;
import com.goumang.sys.api.po.SysUserPo;
import com.goumang.sys.api.vo.SysUserVo;

import java.util.List;
import java.util.Map;

public interface SysUserMapper extends BaseMapper<SysUserPo> {

    /**
     * 通过参数查询
     * @param map
     * @return
     */
    List<SysUserVo> selectByParams(Map<String, Object> map);
}
