package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
import com.goumang.sys.api.po.SysPropertiesPo;
import com.goumang.sys.service.SysPropertiesService;

import java.util.List;

@Service
public class SysPropertiesServiceImpl extends BaseServiceImpl<SysPropertiesPo> implements SysPropertiesService {

    @Override
    public List<SysPropertiesPo> selectForList(SysPropertiesPo po){
        Weekend wk = Weekend.of(SysPropertiesPo.class);
        WeekendCriteria<SysPropertiesPo,Object> criteria = wk.weekendCriteria();

        if(StringUtils.isBlank(po.getOrderBy())){
            po.setOrderBy("propKey");
        }
        if(StringUtils.isNotBlank(po.getPropKey())){
            criteria.andLike(SysPropertiesPo::getPropKey,po.getPropKey()+"%");
        }
        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysPropertiesPo> list = select(wk);
        return list;
    }

}
