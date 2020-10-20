package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import com.goumang.sys.api.po.SysDicPo;
import com.goumang.sys.service.SysDicService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.servlet.ServletContext;
import java.util.*;

@Service
public class SysDicServiceImpl extends BaseServiceImpl<SysDicPo> implements SysDicService {

    private static final String CONSTANT_MAP = "constant_map";

    @Override
    public List<SysDicPo> selectForList(SysDicPo po){
        Weekend wk = Weekend.of(SysDicPo.class);
        WeekendCriteria<SysDicPo,Object> criteria = wk.weekendCriteria();
        MapperUtil.andLike(po, criteria, "code", "name");
        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysDicPo> list = select(wk);
        return list;
    }

    @Override
    public Map<String, List<SysDicPo>> cache(ServletContext sc) {
        SysDicPo po = new SysDicPo();
        po.setOrderBy("type, sort");
        List<SysDicPo> list = selectForList(po);

        Map<String,List<SysDicPo>> map = new HashMap<>();
        String prevType = "";
        List<SysDicPo> typeList = null;
        for(SysDicPo cont : list){
            if(!prevType.equals(cont.getType())){
                typeList = new ArrayList<>();
                prevType = cont.getType();
                map.put(prevType,typeList);
            }
            typeList.add(cont);
        }
        sc.setAttribute(CONSTANT_MAP,map);

        return map;
    }

    @Override
    public List<SysDicPo> listByType(ServletContext sc, String type) {
        Map<String,List<SysDicPo>> map = (Map<String, List<SysDicPo>>) sc.getAttribute(CONSTANT_MAP);
        if(map == null || !map.containsKey(type))  map = cache(sc);

        return map.get(type);
    }

    @Override
    public SysDicPo get(ServletContext sc, String type, String code) {
        Map<String,List<SysDicPo>> map = (Map<String, List<SysDicPo>>) sc.getAttribute(CONSTANT_MAP);
        if(map == null || !map.containsKey(type))  map = cache(sc);

        List<SysDicPo> list = map.get(type);
        Optional<SysDicPo> optional = list.stream().filter(o->code.equals(o.getCode())).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
