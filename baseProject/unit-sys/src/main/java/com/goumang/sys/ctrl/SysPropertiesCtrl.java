package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.sys.api.po.SysPropertiesPo;
import com.goumang.sys.service.SysPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/properties")
public class SysPropertiesCtrl  extends BaseCtrl<SysPropertiesPo> {

    @Autowired
    SysPropertiesService sysPropertiesService;

}
