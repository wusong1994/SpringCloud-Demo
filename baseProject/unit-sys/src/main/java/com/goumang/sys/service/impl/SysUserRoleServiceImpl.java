package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.util.MapperUtil;
import com.goumang.core.util.PojoUtil;
import com.goumang.sys.api.po.SysUserRolePo;
import com.goumang.sys.mapper.SysUserRoleMapper;
import com.goumang.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRolePo> implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysUserRolePo> selectForList(SysUserRolePo po){
        Weekend wk = Weekend.of(SysUserRolePo.class);
        WeekendCriteria<SysUserRolePo,Object> criteria = wk.weekendCriteria();

        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysUserRolePo> list = select(wk);
        return list;
    }

    @Override
    public int batchOperation(Map<String, Object> map) {
        Long roleId = Long.parseLong(map.get("roleId").toString());
        String userIds = (String) map.get("userIds");
        String[] arr = StringUtils.isEmpty(userIds) ? new String[0]: userIds.split(",");

        List<SysUserRolePo> deleteList = select("roleId",roleId);
        List<SysUserRolePo> insertList = new ArrayList<>();
        for(String userId : arr){
            SysUserRolePo userRolePo = new SysUserRolePo();
            userRolePo.setUserId(Long.parseLong(userId));
            userRolePo.setRoleId(roleId);
            insertList.add(userRolePo);
        }
        PojoUtil.intersect(insertList,deleteList,(l,r)->l.getRoleId().equals(r.getRoleId()) && l.getUserId().equals(r.getUserId()));
        for(SysUserRolePo po : insertList){
            insert(po);
        }
        for(SysUserRolePo po : deleteList){
            delete(po.getUsroId());
        }
        return insertList.size();
    }

    @Override
    public Map<String, Set<String>> permissions(Long userId) {
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        List<Map<String,String>> list = sysUserRoleMapper.selectPermissions(params);
        Map<String, Set<String>> permissions = new HashMap<>();
        String prevFuncCode = "";
        Set<String> set = null;
        for(Map<String,String> map : list){
            String funcCode = map.get("funcCode");
            String actionCode = map.get("actionCode");
            if(!funcCode.equals(prevFuncCode)){
                set = new HashSet<>();
                permissions.put(funcCode,set);
                prevFuncCode = funcCode;
            }
            set.add(actionCode);
        }
        return permissions;
    }

    @Override
    public void batchSave(Long userId, String[] roleIds) {
        List<SysUserRolePo> left = select("userId",userId);
        List<SysUserRolePo> right = new ArrayList<>();

        SysUserRolePo userRolePo = null;
        for(String roleId : roleIds){
            userRolePo = new SysUserRolePo();
            userRolePo.setUserId(userId);
            userRolePo.setRoleId(Long.parseLong(roleId));
            right.add(userRolePo);
        }

        PojoUtil.intersect(left,right,(l,r)->l.getRoleId().equals(r.getRoleId()));
        if(!left.isEmpty()) deleteByIds(left.stream().map(o->o.getUsroId().toString()).collect(Collectors.joining(",")));

        right.stream().forEach(o->{
            insert(o);
        });
    }
}
