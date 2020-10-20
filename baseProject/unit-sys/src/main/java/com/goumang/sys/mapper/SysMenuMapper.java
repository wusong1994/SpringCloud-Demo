package com.goumang.sys.mapper;

import com.goumang.core.base.BaseMapper;
import com.goumang.sys.api.po.SysMenuPo;

import java.util.List;
import java.util.Map;

public interface SysMenuMapper extends BaseMapper<SysMenuPo> {

    /**
     * 查询用户菜单
     * @param map
     * @return
     */
    List<SysMenuPo> selectUserMenu(Map<String,Object> map);

    /**
     * 将其它的menu设置为非主页
     * @param menuId
     * @return
     */
    int updateHome(Long menuId);
}
