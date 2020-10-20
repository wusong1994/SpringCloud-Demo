package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
import com.goumang.sys.api.po.SysRolePo;
import com.goumang.sys.service.SysRoleService;

import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRolePo> implements SysRoleService {

    @Override
    public List<SysRolePo> selectForList(SysRolePo po){
        Weekend wk = Weekend.of(SysRolePo.class);
        WeekendCriteria<SysRolePo,Object> criteria = wk.weekendCriteria();

        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysRolePo> list = select(wk);
        return list;
    }

    @Override
    public List<String> getPermissions(String roleCode) {
        return null;
    }
}
