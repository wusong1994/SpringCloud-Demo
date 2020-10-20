package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.core.base.Pager;
import com.goumang.sys.api.po.SysUserPo;
import com.goumang.sys.api.vo.SysUserVo;

import java.util.List;
import java.util.Map;

public interface SysUserService extends BaseService<SysUserPo> {

    List<SysUserVo> selectByParams(Map<String, Object> map);

    Pager<SysUserVo> selectForPage(Map<String, Object> map);

}
