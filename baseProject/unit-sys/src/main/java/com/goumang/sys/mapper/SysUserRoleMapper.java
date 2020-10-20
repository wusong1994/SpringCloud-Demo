package com.goumang.sys.mapper;

import com.goumang.core.base.BaseMapper;
import com.goumang.sys.api.po.SysUserRolePo;

import java.util.List;
import java.util.Map;

public interface SysUserRoleMapper extends BaseMapper<SysUserRolePo> {

    List<Map<String,String>> selectPermissions(Map<String, Object> map);

}
