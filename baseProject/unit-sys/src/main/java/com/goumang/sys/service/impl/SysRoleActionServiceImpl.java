package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import com.goumang.core.util.PojoUtil;
import com.goumang.sys.api.po.SysActionPo;
import com.goumang.sys.api.po.SysRoleActionPo;
import com.goumang.sys.api.po.SysRoleFuncPo;
import com.goumang.sys.mapper.SysActionMapper;
import com.goumang.sys.mapper.SysRoleActionMapper;
import com.goumang.sys.mapper.SysRoleFuncMapper;
import com.goumang.sys.service.SysRoleActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleActionServiceImpl extends BaseServiceImpl<SysRoleActionPo> implements SysRoleActionService {

    @Autowired
    private SysRoleActionMapper sysRoleActionMapper;
    @Autowired
    private SysRoleFuncMapper sysRoleFuncMapper;
    @Autowired
    private SysActionMapper sysActionMapper;

    @Override
    public List<SysRoleActionPo> selectForList(SysRoleActionPo po){
        Weekend wk = Weekend.of(SysRoleActionPo.class);
        WeekendCriteria<SysRoleActionPo,Object> criteria = wk.weekendCriteria();

        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysRoleActionPo> list = select(wk);
        return list;
    }

    @Override
    public int batchInsert(Map<String, Object> map) {
        Integer roleId = Integer.parseInt(map.get("roleId").toString());
        String actionIds = (String) map.get("actionIds");
        if(StringUtils.isEmpty(actionIds)) return 0;
        int i = 0;
        for(String actionId: actionIds.split(",")){
            SysActionPo actionPo = sysActionMapper.selectByPrimaryKey(actionId);
            SysRoleFuncPo sysRoleFuncPo = new SysRoleFuncPo();
            sysRoleFuncPo.setRoleId(roleId.longValue());
            sysRoleFuncPo.setFuncId(actionPo.getFuncId());
            if (sysRoleFuncMapper.select(sysRoleFuncPo).isEmpty()) continue;

            SysRoleActionPo p = new SysRoleActionPo();
            p.setRoleId(roleId.longValue());
            p.setActionId(Long.parseLong(actionId));
            p.setFuncId(actionPo.getFuncId());
            if(this.select(p).isEmpty()){
                this.insertSelective(p);
                i++;
            }
        }
        return i;
    }

    @Override
    public int batchOperation(Map<String, Object> map) {
        Long roleId = Long.parseLong(map.get("roleId").toString());
        Long funcId = Long.parseLong(map.get("funcId").toString());
        String actionIds = (String) map.get("actionIds");
        String[] arr = StringUtils.isEmpty(actionIds) ? new String[0]: actionIds.split(",");

        SysRoleActionPo params = new SysRoleActionPo();
        params.setRoleId(roleId);
        params.setFuncId(funcId);
        List<SysRoleActionPo> insertList = new ArrayList<>();
        List<SysRoleActionPo> deleteList = select(params);
        for(String actionId : arr){
            SysRoleActionPo actionPo = new SysRoleActionPo();
            actionPo.setRoleId(roleId);
            actionPo.setFuncId(funcId);
            actionPo.setActionId(Long.parseLong(actionId));
            insertList.add(actionPo);
        }
        PojoUtil.intersect(insertList,deleteList,(l,r)->l.getRoleId().equals(r.getRoleId()) && l.getActionId().equals(r.getActionId()));
        for(SysRoleActionPo po : insertList){
            insertSelective(po);
        }
        for(SysRoleActionPo po : deleteList){
            delete(po.getRoacId());
        }
        return insertList.size();
    }

}
