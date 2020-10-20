package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
import com.goumang.sys.api.po.SysDicTypePo;
import com.goumang.sys.service.SysDicTypeService;

import java.util.List;

@Service
public class SysDicTypeServiceImpl extends BaseServiceImpl<SysDicTypePo> implements SysDicTypeService {

    @Override
    public List<SysDicTypePo> selectForList(SysDicTypePo po){
        Weekend wk = Weekend.of(SysDicTypePo.class);
        WeekendCriteria<SysDicTypePo,Object> criteria = wk.weekendCriteria();

        MapperUtil.andLike(po, criteria, "code","name");
        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysDicTypePo> list = select(wk);
        return list;
    }

}
