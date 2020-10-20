package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import com.goumang.core.util.PojoUtil;
import com.goumang.sys.api.po.SysRoleActionPo;
import com.goumang.sys.api.po.SysRoleFuncPo;
import com.goumang.sys.mapper.SysRoleActionMapper;
import com.goumang.sys.service.SysRoleFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleFuncServiceImpl extends BaseServiceImpl<SysRoleFuncPo> implements SysRoleFuncService {

    @Autowired
    private SysRoleActionMapper sysRoleActionMapper;

    @Override
    public List<SysRoleFuncPo> selectForList(SysRoleFuncPo po){
        Weekend wk = Weekend.of(SysRoleFuncPo.class);
        WeekendCriteria<SysRoleFuncPo,Object> criteria = wk.weekendCriteria();

        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysRoleFuncPo> list = select(wk);
        return list;
    }

    /**
     * 同时删除角色操作点权限
     * @param key
     * @return
     */
    @Override
    public int delete(Object key) {
        SysRoleFuncPo po = get(key);
        if(po==null) return 0;

        SysRoleActionPo roleActionPo = new SysRoleActionPo();
        roleActionPo.setRoleId(po.getRoleId());
        roleActionPo.setFuncId(po.getFuncId());
        sysRoleActionMapper.delete(roleActionPo);

        return super.delete(key);
    }

    @Override
    public int batchOperation(Map<String, Object> map) {
        Long roleId = Long.parseLong(map.get("roleId").toString());
        String funcIds = (String) map.get("funcIds");

        String[] arr = StringUtils.isEmpty(funcIds) ? new String[0]: funcIds.split(",");

        SysRoleFuncPo roleFuncParams = new SysRoleFuncPo();
        roleFuncParams.setRoleId(roleId);
        List<SysRoleFuncPo> deleteList = select(roleFuncParams);
        List<SysRoleFuncPo> insertList = new ArrayList<>();
        for(String funcId: arr){
            SysRoleFuncPo p = new SysRoleFuncPo();
            p.setRoleId(roleId.longValue());
            p.setFuncId(Long.parseLong(funcId));
            insertList.add(p);
        }
        PojoUtil.intersect(insertList,deleteList,(insert,delete)->insert.getFuncId().equals(delete.getFuncId()));
        for(SysRoleFuncPo po : insertList){
            this.insert(po);
        }
        for(SysRoleFuncPo po : deleteList){
            this.delete(po.getPk());
            //删除操作点权限
            SysRoleActionPo roleActionParam = new SysRoleActionPo();
            roleActionParam.setRoleId(po.getRoleId());
            roleActionParam.setFuncId(po.getFuncId());
            sysRoleActionMapper.delete(roleActionParam);
        }
        return insertList.size();
    }

}
