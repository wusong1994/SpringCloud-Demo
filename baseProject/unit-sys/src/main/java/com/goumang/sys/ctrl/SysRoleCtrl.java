package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.sys.api.po.SysRolePo;
import com.goumang.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/role")
public class SysRoleCtrl  extends BaseCtrl<SysRolePo> {

    @Autowired
    SysRoleService sysRoleService;



}
