package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.ErrorUtil;
import com.goumang.core.util.MapperUtil;
import com.goumang.sys.api.po.SysMenuPo;
import com.goumang.sys.api.vo.SysMenuVo;
import com.goumang.sys.mapper.SysMenuMapper;
import com.goumang.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuPo> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuPo> selectForList(SysMenuPo po){
        Weekend wk = Weekend.of(SysMenuPo.class);
        WeekendCriteria<SysMenuPo,Object> criteria = wk.weekendCriteria();

        MapperUtil.andLike(po, criteria, "menuTitle");
        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysMenuPo> list = select(wk);
        return list;
    }

    @Override
    public List<SysMenuPo> selectUserMenu(Long userId) {
        if(userId==null || userId<=0){
            SysMenuPo params = new SysMenuPo();
            params.setOrderBy("parentId, sort");
            return selectForList(params);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        return sysMenuMapper.selectUserMenu(map);
    }

    @Override
    public void setDefault(Long menuId) {
        SysMenuPo po = get(menuId);
        if(po.getParentId()!=0) ErrorUtil.error("首页必须为一级菜单");
        if(!po.getHome()){
            po.setHome(true);
            update(po);
        }

        sysMenuMapper.updateHome(menuId);
    }

    @Override
    public void move(SysMenuVo vo, Integer position) {
        SysMenuPo param = new SysMenuPo();
        param.setParentId(vo.getParentId());
        param.setColumnName("sort");
        Integer max = getMax(param,Integer.class);


        SysMenuPo po = get(vo.getPk());
        po.setParentId(vo.getParentId());
        po.setSort(max.longValue()+1);
        this.updateSelective(po);

        param = new SysMenuPo();
        param.setParentId(vo.getParentId());
        param.setPk(vo.getPk());
        param.setFieldName("sort");
        this.updateSort(param,position);
    }
}
