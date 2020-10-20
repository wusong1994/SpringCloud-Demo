package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysActionPo;
import com.goumang.sys.service.SysActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sys/action")
public class SysActionCtrl  extends BaseCtrl<SysActionPo> {

    @Autowired
    SysActionService sysActionService;

    /** 添加默认的操作点 */
    @RequestMapping(value = "/addDefault/{funcId}", method = RequestMethod.POST)
    public WebResponse addDefault(@PathVariable("funcId") Long funcId){
        return new WebResponse(sysActionService.addDefault(funcId));
    }

    /** 分页连接角色 */
    @RequestMapping(value = "/pageJoinRole", method = RequestMethod.POST)
    public WebResponse pageJoinRole(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"funcId","roleId");
        return new WebResponse(sysActionService.selectForPage(map));
    }
}
