package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysMenuPo;
import com.goumang.sys.api.vo.SysMenuVo;
import com.goumang.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuCtrl  extends BaseCtrl<SysMenuPo> {

    @Autowired
    SysMenuService sysMenuService;

    @Override
    public SysMenuPo preInsert(SysMenuPo po) {
        ParamUtil.notBlank(po,"parentId","menuTitle");
        SysMenuPo params = new SysMenuPo();
        params.setParentId(po.getParentId());
        params.setColumnName("sort");
        Long nextSort = sysMenuService.getMax(params,Integer.class) + 1L;
        po.setSort(nextSort);
        return super.preInsert(po);
    }

    @Override
    public void postInsert(SysMenuPo po, int i) {
        if(po.getHome()!=null && po.getHome()){
            sysMenuService.setDefault(po.getMenuId());
        }
        super.postInsert(po, i);
    }

    @Override
    public SysMenuPo preUpdate(SysMenuPo po) {
        if(po.getHome()){
            sysMenuService.setDefault(po.getMenuId());
        }
        return super.preUpdate(po);
    }

    /** 树形查询 */
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public WebResponse tree(@RequestBody SysMenuPo po){
        ParamUtil.notBlank(po,"parentId");
        po.setFieldName("parentId");
        po.setOrderBy("parentId, sort");
        return new WebResponse(sysMenuService.selectForTree(po,false));
    }

    /** 树形删除 */
    @RequestMapping(value = "/deleteTree/{menuId}", method = RequestMethod.DELETE)
    public WebResponse deleteTree(@PathVariable("menuId") Long menuId){
        sysMenuService.deleteForTree(menuId, "parentId");
        return new WebResponse();
    }
    
    /** 查询用户菜单 */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public WebResponse userMenu(@PathVariable("userId") Long userId){
        return new WebResponse(sysMenuService.selectUserMenu(userId));
    }

    /** 排序 */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public WebResponse sort(@RequestBody SysMenuVo vo){
        ParamUtil.notBlank(vo, "pk","parentId","fieldName","position");
        SysMenuPo po = new SysMenuPo();
        po.setPk(vo.getPk());
        po.setParentId(vo.getParentId());
        po.setFieldName(vo.getFieldName());
        sysMenuService.updateSort(po, vo.getPosition());
        return new WebResponse();
    }

    /** 移动 */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public WebResponse move(@RequestBody SysMenuVo vo){
        ParamUtil.notBlank(vo,"pk","parentId","position");
        sysMenuService.move(vo, vo.getPosition());
        return new WebResponse();
    }
}
