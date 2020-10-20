package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.sys.api.po.SysMenuPo;
import com.goumang.sys.api.vo.SysMenuVo;

import java.util.List;


public interface SysMenuService extends BaseService<SysMenuPo> {

    /**
     * 查询用户菜单
     * @param userId 用户ID
     * @return
     */
    List<SysMenuPo> selectUserMenu(Long userId);

    /**
     * 设置默认菜单
     * @param menuId
     */
    void setDefault(Long menuId);

    /**
     * 移动
     * @param vo
     * @param position
     */
    void move(SysMenuVo vo, Integer position);
}
