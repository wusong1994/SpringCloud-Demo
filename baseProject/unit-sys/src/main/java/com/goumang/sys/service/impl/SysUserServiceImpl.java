package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.base.Pager;
import com.goumang.core.util.MapperUtil;
import com.goumang.sys.api.po.SysUserPo;
import com.goumang.sys.api.vo.SysUserVo;
import com.goumang.sys.mapper.SysUserMapper;
import com.goumang.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserPo> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUserPo> selectForList(SysUserPo po){
        Weekend wk = Weekend.of(SysUserPo.class);
        WeekendCriteria<SysUserPo,Object> criteria = wk.weekendCriteria();

        MapperUtil.andLike(po,criteria,"userName","loginName","phone");
        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysUserPo> list = select(wk);
        return list;
    }

    @Override
    public List<SysUserVo> selectByParams(Map<String, Object> map) {
        return sysUserMapper.selectByParams(map);
    }

    @Override
    public Pager<SysUserVo> selectForPage(Map<String, Object> map) {
        return selectForPage(o->selectByParams(o),map);
    }

}
